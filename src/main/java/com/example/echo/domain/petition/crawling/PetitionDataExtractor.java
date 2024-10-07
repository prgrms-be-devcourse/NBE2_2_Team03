package com.example.echo.domain.petition.crawling;

import com.example.echo.domain.petition.entity.Category;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetitionDataExtractor {

    public static LocalDateTime extractStartDate(String period) {
        // 패턴 (e.g., 2024-08-30)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] parts = period.split("~");

        // 시작일 값 받기
        String startDateString = parts[0].replace("동의기간", "").trim();

        // string -> localdate
        LocalDate startDate = LocalDate.parse(startDateString, formatter);

        // 그냥 LocalDate 로 받아도 되지 않나.
        return startDate.atStartOfDay();
    }

    public static LocalDateTime extractEndDate(String period) {
        // 패턴
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] parts = period.split("~");

        // 종료일
        String endDateString = parts[1].trim();

        // string -> localdate
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        // 그냥 LocalDate 로 받아도 되지 않나.
        return endDate.atStartOfDay();
    }

    public static String extractNumber(String text) {
        String regex = "\\d{1,3}(,\\d{3})*"; // 데이터 찾기 위한 정규 표현식
        Pattern pattern = Pattern.compile(regex);   // 정의한 정규 표현식 컴파일하여 패턴 객체 생성
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) { // 맞는 값 찾고 숫자 추출하기
            return matcher.group().replace(",", "");
        }
        return null;
    }

    public static Category convertCategory(String category) {
        Category categoryEnum = Category.fromDescription(category); // 해당하는 카테고리 enum 찾기

        if (categoryEnum == null) {
            System.out.println("Unknown category: " + category);
        }
        return categoryEnum;
    }
}
