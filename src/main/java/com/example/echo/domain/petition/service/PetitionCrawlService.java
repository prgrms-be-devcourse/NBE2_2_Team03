package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.entity.crawling.PetitionCrawl;
import com.example.echo.domain.petition.entity.crawling.PetitionDataExtractor;
import com.example.echo.domain.petition.repository.PetitionRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetitionCrawlService {

    @Autowired
    private PetitionRepository petitionRepository;

    private static final Logger logger = LoggerFactory.getLogger(PetitionCrawlService.class);

    //title, href 이런 것들 따로 리스트에 저장한 뒤에
    //petition 다시 돌면서 href 같이 돌면서 들어가서 정보 가져오기
    public String dynamicCrawl(String url) {
        // ChromeDriver 주소
        System.setProperty("webdriver.chrome.driver", "C:/webprac/chromedriverwin32/chromedriver.exe");
        // 옵셜 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
        StringBuilder result = new StringBuilder();

        // href 이용을 위한 초기 크롤링 데이터 저장
        List<PetitionCrawl> crawledData = new ArrayList<>();

        try {
            driver.get(url);

            int waitDuration = 20;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));

            // 마지막 페이지 무한 출력 해결을 위한 이전 제목들 저장
            List<String> previousTitles = new ArrayList<>();

            while (true) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".list_card")));
                List<WebElement> petitionCards = driver.findElements(By.cssSelector(".item_card"));

                //////////
                // 마지막 페이지 무한 출력 해결을 위한 현재 페이지 제목들 저장
                List<String> currentTitles = new ArrayList<>();
                for (WebElement petition : petitionCards) {
                    try {
                        String title = petition.findElement(By.cssSelector(".desc")).getText();
                        currentTitles.add(title);
                    } catch (StaleElementReferenceException e) {
                        System.out.println("Stale element. Retrying...");
                        break;
                    }
                }

                // 제목 값들 비교
                if (currentTitles.equals(previousTitles)) {
                    System.out.println("No more new content to load. Ending crawl.");
                    System.out.println(currentTitles);
                    System.out.println(previousTitles);
                    break;
                }

                //  if 문 안걸렸으면 다음 페이지로 이동 후 비교를 위해 현재 제목들 이전 제목으로 설정
                previousTitles = new ArrayList<>(currentTitles);
                //////////

                int countPetition = 0;
                // petition 크롤링
                for (WebElement petition : petitionCards) {
                    try {
                        // petition 하나마다 값 가져오고 href 로 넘어가서 내용 받아오기

                        // 이 부분 진행하며 값 db에 있나 체크
                        /////// 상세 페이지 넘어가서 데이터 가져오기 - url 값으로 데이터 있으면 pass 하기
                        WebElement link = petition.findElement(By.tagName("a")); // a태그 찾기
                        String href = link.getAttribute("href"); // a 태그의 href 속성으로 넘어가서 값 받아오기 구현 필요
                        Optional<Petition> alreadyExistPetition = petitionRepository.findByUrl(href);
                        if (alreadyExistPetition.isPresent()) {
                            continue;
                        }

                        String title = petition.findElement(By.cssSelector(".desc")).getText();
                        String period = petition.findElement(By.cssSelector(".period")).getText();
                        String category = petition.findElement(By.cssSelector(".category")).getText();
                        String count = petition.findElement(By.cssSelector(".count")).getText(); //parseInt 사용 안함.

                        PetitionCrawl petitionObj = new PetitionCrawl(title, period, category, count, href, null);
                        crawledData.add(petitionObj);

                        result.append(title).append(" ").append(category).append(" ").append(href).append("\n");
                        countPetition++;

                    } catch (Exception e) {
                        System.out.println(countPetition);
                        System.out.println("error extracting data : " + e.getMessage());
                    }

                }

                //////////////
                // 페이지 핸들링
                if (!navigateToNextPage(driver, wait)) {
                    break;
                }
            }

            for (PetitionCrawl eachData : crawledData) {
                fetchPetitionDetails(driver, wait, eachData);
                //content 추가 확인
                // System.out.println(eachData.getTitle() + "\n" + eachData.getContent());
                System.out.println("Title: " + eachData.getTitle() + "\n"
                        + "Period: " + eachData.getPeriod() + "\n"
                        + "Category: " + eachData.getCategory() + "\n"
                        + "AgreeCount: " + eachData.getAgreeCount() + "\n"
                        + "Original Url Href: " + eachData.getHref() + "\n"
                        + "Content" + eachData.getContent());
                result.append(eachData.getTitle()).append("\n").append(eachData.getContent());
            }
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            //return null;
        } finally {
            driver.quit();
        }
        // 크롤링 완료

        return result.toString();
    }

    private boolean navigateToNextPage(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.next-button")));
            if (!nextButton.isEnabled()) {
                return false; // No more pages to load
            }
            nextButton.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".list_card")));
            return true;
        } catch (StaleElementReferenceException e) {
            System.out.println("Stale element on pagination, retrying...");
            return false;
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("No more pages or next button not found.");
            return false;
        }
    }

    private void fetchPetitionDetails(WebDriver driver, WebDriverWait wait, PetitionCrawl petitionCrawl) {
        try {
            driver.get(petitionCrawl.getHref());
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            WebElement contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pre.contentTxt")));
            String content = contentElement.getText();
            petitionCrawl.changeContent(content);
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("Could not retrieve petition details: " + e.getMessage());
        }
    }
}



        ////////////////////////////////////
//        // DB 에 값 없으면 DB에 값 집어 넣기
//        for (PetitionCrawl eachData : crawledData) {
//
//            // 테이블에 데이터 추가
//            // 크롤링 데이터 타입 수정
//
//            // 카테고리 형식 바꾸기
//
//
//            String title = eachData.getTitle();
//            // 기간으로 설정 된 값에서 시작일, 종료일 뽑아내기
//            LocalDateTime startDate = PetitionDataExtractor.extractStartDate(eachData.getPeriod());
//            LocalDateTime endDate = PetitionDataExtractor.extractEndDate(eachData.getPeriod());
//            Category category = PetitionDataExtractor.convertCategory(eachData.getCategory());
//            // 명 제외하고 int 형으로 바꾸기
//            int agreeCount = Integer.parseInt(PetitionDataExtractor.extractNumber(eachData.getAgreeCount()));
//            String originalUrl = eachData.getHref();
//            String content = eachData.getContent();
//
//            Petition petition = Petition.builder()
//                    .title(title)
//                    .startDate(startDate)
//                    .endDate(endDate)
//                    .category(category)
//                    .agreeCount(agreeCount)
//                    .originalUrl(originalUrl)
//                    .content(content)
//                    .build();
//            petitionRepository.save( petition );
//        }


