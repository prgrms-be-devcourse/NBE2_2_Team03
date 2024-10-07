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
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
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

    private WebDriver driver;
    private WebDriverWait wait;

    public PetitionCrawlService() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    //title, href 이런 것들 따로 리스트에 저장한 뒤에
    //petition 다시 돌면서 href 같이 돌면서 들어가서 정보 가져오기
    public List<PetitionCrawl> dynamicCrawl(Long id, String url) {
        // 옵션 설정
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//
//        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(390, 844)); // 크롤링 화면 크기 설정
        StringBuilder result = new StringBuilder();

        // href 이용을 위한 초기 크롤링 데이터 저장
        List<PetitionCrawl> crawledData = new ArrayList<>();
        try {
            driver.get(url);

            int waitDuration = 30;
            wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));

            // 마지막 페이지 무한 출력 해결을 위한 이전 제목들 저장
            List<String> previousTitles = new ArrayList<>();

            while (true) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".list_card")));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".item_card")));
                List<WebElement> petitionCards = driver.findElements(By.cssSelector(".item_card"));
                for (WebElement petitionCard : petitionCards) {
                    System.out.println(petitionCard.getText());
                }
                // 이전 페이지의 제목 목록과 비교하는 작업 필요
                // 새로운 페이지의 제목 목록
                // 비교해서 다르면 대기
                // 페이지 끝나면 새로운 페이지 목록을 이전 페이지 목록으로 설정

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

        return crawledData; //result.toString();
    }

    private boolean navigateToNextPage(WebDriver driver, WebDriverWait wait, int currentPageNum, int totalPagesNum,
                                       List<WebElement> petitionCards) {
        try {
            if (currentPageNum == totalPagesNum) {
                return false;
            }
            Thread.sleep(100);
            List<String> oldTitles = petitionCards.stream()
                    .map(card -> card.findElement(By.cssSelector(".desc")).getText())
                    .collect(Collectors.toList());
            for (String title : oldTitles) {
                System.out.println(title);
            }

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.next-button")));
            nextButton.click();

            Thread.sleep(100);
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));

            Thread.sleep(100);
            List<WebElement> newPetitionCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".item_card")));

            Thread.sleep(100);
            try {
                newPetitionCards.stream()
                        .map(card -> card.findElement(By.cssSelector(".desc")).getText())
                        .collect(Collectors.toList());
            } catch (StaleElementReferenceException e) {
                System.out.println("new page desc found err");
            }
            List<String> newTitles = newPetitionCards.stream()
                    .map(card -> card.findElement(By.cssSelector(".desc")).getText())
                    .collect(Collectors.toList());

            for (String title : newTitles) {
                System.out.println(title);
            }


            while (oldTitles.equals(newTitles)) {
                newPetitionCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".item_card")));
                newTitles = newPetitionCards.stream()
                        .map(card -> card.findElement(By.cssSelector(".desc")).getText())
                        .collect(Collectors.toList());
            }
            return true;

        } catch (StaleElementReferenceException e) {
            logger.error("StaleElementReferenceException while paging: ", e);
            System.out.println("Stale element on pagination, retrying...");
            return false;
        } catch (TimeoutException | NoSuchElementException e) {
            logger.error("TimeoutException | NoSuchElementException while paging : ", e);
            System.out.println("No more pages or next button not found.");
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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



    // 동의자 수 업데이트
    public int fetchAgreeCount(String url) {
        driver.get(url); // 청원 url
        // 현재 웹의 동의 수가 0으로 나오는 경우 발생. wait 이용 대기
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        // 동의자 수 추출
        String agreeCountText = driver.findElement(By.cssSelector(".count")).getText();
        String agreeCountNum = PetitionDataExtractor.extractNumber(agreeCountText);
        return Integer.parseInt(agreeCountNum);
    }

    public void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }


}