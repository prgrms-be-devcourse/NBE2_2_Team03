package com.example.echo.domain.petition.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.repository.MemberRepository;
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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class PetitionCrawlService {

    @Autowired
    private PetitionRepository petitionRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(PetitionCrawlService.class);

    //title, href 이런 것들 따로 리스트에 저장한 뒤에
    //petition 다시 돌면서 href 같이 돌면서 들어가서 정보 가져오기
    public String dynamicCrawl(Long id, String url) {
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
                for (WebElement petitionCard : petitionCards) {
                    System.out.println(petitionCard.getText());
                }

                int countPetition = 0;
                // petition 크롤링
                for (WebElement petition : petitionCards) {  // 여기 두 번 반복되어 들어가 있음
                    // 아래 crawledData checkNum 3이어야하는데 6
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
                        System.out.println(countPetition);


                    } catch (Exception e) {
                        System.out.println(countPetition);
                        System.out.println("error extracting data : " + e.getMessage());
                    }

                }

                // 현재 페이지와 마지막 페이지 비교 같으면 종료
                WebElement pagingElement = driver.findElement(By.cssSelector("div.mobile_paging span"));
                String currentPage = pagingElement.findElement(By.tagName("em")).getText();
                String fullText = pagingElement.getText();
                String[] pageNumbers = fullText.split("/");
                int currentPageNum = Integer.parseInt(pageNumbers[0].trim()); // "7"
                int totalPagesNum = Integer.parseInt(pageNumbers[1].trim());
                System.out.println("Current Page: " + currentPageNum);
                System.out.println("Total Pages: " + totalPagesNum);

                for (PetitionCrawl petitionCrawl : crawledData) {
                    System.out.println(petitionCrawl.getTitle());
                }

                // 페이지 비교 통과면 다음 페이지로 넘기기
                if (!navigateToNextPage(driver, wait, currentPageNum, totalPagesNum, petitionCards)) {
                    break;
                }

            }
//
            int checkNum = 0;

            for (PetitionCrawl eachData : crawledData) {
                checkNum++;
                try {
                    fetchPetitionDetails(driver, wait, eachData);
                } catch (StaleElementReferenceException e) {
                    log.error("Stale element reference. Retrying petition details fetching for {}", eachData.getHref());
                    fetchPetitionDetails(driver, wait, eachData);
                }

                String title = eachData.getTitle();
                log.info("Processing petition: Title={}, Period={}, Category={}", eachData.getTitle(), eachData.getPeriod(), eachData.getCategory());
                // 기간으로 설정 된 값에서 시작일, 종료일 뽑아내기
                LocalDateTime startDate = PetitionDataExtractor.extractStartDate(eachData.getPeriod());
                log.info("startDate={}", startDate);
                LocalDateTime endDate = PetitionDataExtractor.extractEndDate(eachData.getPeriod());
                log.info("endDate={}", endDate);
                Category category = PetitionDataExtractor.convertCategory(eachData.getCategory());
                log.info("category={}", category);
                // 명 제외하고 int 형으로 바꾸기
                int agreeCount = Integer.parseInt(PetitionDataExtractor.extractNumber(eachData.getAgreeCount()));
                log.info("agreeCount={}", agreeCount);
                String originalUrl = eachData.getHref();
                log.info("originalUrl={}", originalUrl);
                String content = eachData.getContent();
                log.info("content={}", content);

                Member member = memberRepository.findById(id)
                        .orElseThrow(()-> new RuntimeException("회원정보를 찾을수 없습니다."));

                Petition petitionSave = Petition.builder()
                        .member(member)
                        .title(title)
                        .startDate(startDate)
                        .endDate(endDate)
                        .category(category)
                        .agreeCount(agreeCount)
                        .originalUrl(originalUrl)
                        .content(content)
                        .build();
                petitionRepository.save( petitionSave );

                System.out.println("Saved Petition: " + petitionSave.getTitle());
///////////////////////////
            }

            System.out.println(checkNum);
        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            //return null;
        } finally {
            driver.quit();
        }
        // 크롤링 완료

        return crawledData.toString(); //result.toString();
    }

    private boolean navigateToNextPage(WebDriver driver, WebDriverWait wait, int currentPageNum, int totalPagesNum,
                                       List<WebElement> petitionCards) {
        try {
            if (currentPageNum == totalPagesNum) {
                return false;
            }

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.next-button")));
            nextButton.click();
            List<WebElement> newPetitionCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".item_card")));
            while (petitionCards.get(0) == newPetitionCards.get(0)) {
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".item_card")));
            }
            return true;
//            // 페이지 stale 이용 시 err 발생 대신 새로운 list 먼저 생성 후 첫번째 값 비교
//
        } catch (StaleElementReferenceException e) {
            logger.error("StaleElementReferenceException while paging: ", e);
            System.out.println("Stale element on pagination, retrying...");
            return false;
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("TimeoutException | NoSuchElementException while paging : ", e);
            System.out.println("No more pages or next button not found.");
            return false;
        }
    }

    private void fetchPetitionDetails(WebDriver driver, WebDriverWait wait, PetitionCrawl petitionCrawl) {
        try {
            while (petitionCrawl.getContent() == null) {
                driver.get(petitionCrawl.getHref());
                wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                WebElement contentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pre.contentTxt")));
                wait.until(webDriver -> !contentElement.getText().trim().isEmpty());
                String content = contentElement.getText();
                petitionCrawl.changeContent(content);
            }
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("TimeoutException | NoSuchElementException while fetching: ", e);
        }
    }

}

