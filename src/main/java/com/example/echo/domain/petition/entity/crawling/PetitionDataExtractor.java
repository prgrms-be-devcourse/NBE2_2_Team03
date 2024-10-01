package com.example.echo.domain.petition.entity.crawling;

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
        String regex = "\\d{1,3}(,\\d{3})*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group().replace(",", "");
        }
        return null;
    }

    public static Category convertCategory(String category) {
        Category categoryEnum = Category.fromDescription(category);
        if (categoryEnum == null) {
            System.out.println("Unknown category: " + category);
        }
        return categoryEnum;
    }

}
