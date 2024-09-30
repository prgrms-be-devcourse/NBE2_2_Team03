package com.example.echo.domain.petition.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class PagingRequestDto {
    private int page = 0;
    private int size = 10;
    private String sortBy = "agreeCount";
    private String direction = "desc"; // 동의자 순 나열

    public Pageable toPageable() {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}
