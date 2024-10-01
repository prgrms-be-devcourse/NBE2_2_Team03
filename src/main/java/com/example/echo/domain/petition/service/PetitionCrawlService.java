package com.example.echo.domain.petition.service;

import com.example.echo.domain.petition.entity.PetitionCrawl;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PetitionCrawlService {
    private static final Logger logger = LoggerFactory.getLogger(PetitionCrawlService.class);

    //title, href 이런 것들 따로 리스트에 저장한 뒤에
    //petiton 다시 돌면서 href 같이 돌면서 들어가서 정보 가져오기

    public String dynamicCrawl(String url) {
        // ChromeDriver 주소
        System.setProperty("webdriver.chrome.driver", "C:/webprac/chromedriver-win64/chromedriver.exe");
        // 옵셜 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);

        StringBuilder result = new StringBuilder();

        try {
            driver.get(url);

            int waitDuration = 10;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitDuration));
            WebElement plist = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(".list_card")) // table id로 찾기
            );

            // href 이용을 위한 초기 크롤링 데이터 저장
            List<PetitionCrawl> crawledData = new ArrayList<>();

            // 마지막 페이지 무한 출력 해결을 위한 이전 제목들 저장
            List<String> previousTitles = new ArrayList<>();

            while (true) {
                List<WebElement> petitionCards = plist.findElements(By.cssSelector(".item_card"));

                //////////
                // 마지막 페이지 무한 출력 해결을 위한 현재 페이지 제목들 저장
                List<String> currentTitles = new ArrayList<>();

                for (WebElement petition : petitionCards) {
                    String title = petition.findElement(By.cssSelector(".desc")).getText();
                    currentTitles.add(title);
                }

                // 제목 값들 비교
                if (currentTitles.equals(previousTitles)) {
                    System.out.println("No more new content to load. Ending crawl.");
                    break;
                }


                //  if 문 안걸렸으면 다음 페이지로 이동 후 비교를 위해 현재 제목들 이전 제목으로 설정
                previousTitles = new ArrayList<>(currentTitles);
                //////////

                // petiton 크롤링
                for (WebElement petition : petitionCards) {
                    // petition 하나마다 값 가져오고 href 로 넘어가서 내용 받아오기

                    String title = petition.findElement(By.cssSelector(".desc")).getText();
                    String period = petition.findElement(By.cssSelector(".period")).getText();
                    String category = petition.findElement(By.cssSelector(".category")).getText();
                    String count = petition.findElement(By.cssSelector(".count")).getText(); //parseInt 사용 안함.


                    /////// 상세 페이지 넘어가서 데이터 가져오기
                    WebElement link = petition.findElement(By.tagName("a")); // a태그 찾기
                    String href = link.getAttribute("href"); // a 태그의 href 속성으로 넘어가서 값 받아오기 구현 필요

                    PetitionCrawl petitionObj = new PetitionCrawl(title, period, category, count, href,null);
                    crawledData.add(petitionObj);

                    result.append(title).append(" ").append(category).append(" ").append(href).append("\n");
                    System.out.println("Title: " + title + "Period: " + period + " Category: " + category + "Count: " + count +
                            " Href: " + href);
                    // System.out.println(petitionObj);
                    System.out.println();
                }

                //////////////
                // 페이지 핸들링

                try {
                    WebElement nextButton = driver.findElement(By.cssSelector("button.btn.next-button"));
                    nextButton.click();  // click
                    Thread.sleep(2000);  // 페이지 로딩 대기
                } catch (Exception e) {
                    System.out.println("No more pages to load.");
                    break;  // 페이지 끝
                }
            }

            for (PetitionCrawl eachData : crawledData) {
                driver.get(eachData.getHref());
                new WebDriverWait(driver, Duration.ofSeconds(waitDuration));

                String content = driver.findElement(By.cssSelector(".pre.contentTxt")).getText();
                eachData.changeContent(content);

                //content 추가 확인
                System.out.println(eachData.getTitle() + "\n" + eachData.getContent());
                result.append(eachData.getTitle()).append("\n").append(eachData.getContent());
            }

            return result.toString();

        } catch (Exception e) {
            logger.error("An error occurred: ", e);
            throw new RuntimeException("크롤링 중 오류가 발생했습니다. 입력한 URL을 확인해 주세요."); // 오류 처리 추가
        } finally {
            driver.quit();
        }
    }

}
