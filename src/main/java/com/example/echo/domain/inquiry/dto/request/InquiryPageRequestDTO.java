package com.example.echo.domain.inquiry.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryPageRequestDTO {

    @NotNull(message = "페이지 번호는 필수입니다.")
    @Min(value = 1, message = "페이지 번호는 최소 1이어야 합니다.")
    @Builder.Default
    private Integer pageNumber = 1;

    @NotNull(message = "페이지 사이즈는 필수입니다.")
    @Min(value = 5, message = "페이지 사이즈는 최소 5이어야 합니다.")
    @Max(value = 20, message = "페이지 사이즈는 최대 20이어야 합니다.")
    @Builder.Default
    private Integer pageSize = 5;

    public PageRequest getPageable() {
        return PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by("inquiryId").descending()); // 항상 최신 순 정려
    }
}
