package com.example.echo.domain.inquiry.repository;

import com.example.echo.domain.inquiry.dto.response.InquiryResponse;
import com.example.echo.domain.inquiry.entity.Inquiry;
import com.example.echo.domain.inquiry.entity.QInquiry;
import com.example.echo.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.query.Param;

import java.util.List;

public class InquiryPagingImpl extends QuerydslRepositorySupport implements InquiryPaging {

    public InquiryPagingImpl() {
        super(Inquiry.class);
    }

    @Override
    public Page<InquiryResponse> findAllInquiriesUser(@Param("memberId") Long memberId, Pageable pageable) {
        QInquiry inquiry = QInquiry.inquiry;
        QMember member = QMember.member;

        // Inquiry와 Member를 left join하고 memberId로 필터링
        JPQLQuery<InquiryResponse> query = from(inquiry)
                .leftJoin(inquiry.member, member)
                .where(member.memberId.eq(memberId))
                .select(Projections.bean(InquiryResponse.class,  // 프로젝션
                        inquiry.inquiryId,
                        member.memberId.as("memberId"),
                        inquiry.inquiryCategory,
                        inquiry.inquiryTitle,
                        inquiry.inquiryContent,
                        inquiry.createdDate,
                        inquiry.replyContent,
                        inquiry.inquiryStatus,
                        inquiry.repliedDate
                ));

        getQuerydsl().applyPagination(pageable, query);  // 페이징 적용

        List<InquiryResponse> content = query.fetch();    // 쿼리 실행 결과
        long total = query.fetchCount(); // 총 개수

        return new PageImpl<>(content, pageable, total);
    }
}
