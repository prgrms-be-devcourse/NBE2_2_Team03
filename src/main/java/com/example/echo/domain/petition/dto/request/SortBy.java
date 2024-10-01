package com.example.echo.domain.petition.dto.request;

public enum SortBy {
    AGREE_COUNT("agreeCount"),
    LIKES_COUNT("likesCount"),
    END_DATE("endDate"),
    CATEGORY("category");

    private final String field;

    SortBy(String field) {
        this.field = field;
    }

    public String getField() {
        return this.field;
    }
}
