package com.example.nbe2_2_team03.entity;

import lombok.Getter;

@Getter
public enum Category {
    POLITICS_ELECTION_PARLIAMENT("정치/선거/국회운영"),
    INVESTIGATION_LAW_JUDICIAL_SYSTEM("수사/법무/사법제도"),
    FINANCE_TAX_FINANCIAL_BUDGET("재정/세제/금융/예산"),
    CONSUMER_FAIR_TRADE("소비자/공정거래"),
    EDUCATION("교육"),
    SCIENCE_TECHNOLOGY_INFORMATION_COMMUNICATION("과학기술/정보통신"),
    DIPLOMACY_UNIFICATION_NATIONAL_DEFENSE_SECURITY("외교/통일/국방/안보"),
    DISASTER_SAFETY_ENVIRONMENT("재난/안전/환경"),
    ADMINISTRATION_LOCAL_AUTONOMY("행정/지방자치"),
    CULTURE_SPORTS_TOURISM_MEDIA("문화/체육/관광/언론"),
    AGRICULTURE_FORESTRY_FISHERY_ANIMAL_HUSBANDRY("농업/임업/수산업/축산업"),
    INDUSTRY_TRADE("산업/통상"),
    HEALTHCARE("보건의료"),
    WELFARE_VETERANS("복지/보훈"),
    LAND_OCEAN_TRANSPORTATION("국토/해양/교통"),
    HUMAN_RIGHTS_GENDER_EQUALITY_LABOR("인권/성평등/노동"),
    LOW_BIRTHRATE_AGING_CHILDREN_YOUTH_FAMILY("저출산/고령화/아동/청소년/가족"),
    OTHERS("기타");

    private final String description;

    Category(String description) {
        this.description = description;
    }
}
