package com.example.echo.domain.petition.repository;

import com.example.echo.domain.petition.entity.Category;
import com.example.echo.domain.petition.dto.response.PetitionResponseDto;
import com.example.echo.domain.petition.entity.Petition;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
    Page<Petition> findByCategory(Pageable pageable, Category category);

    @Query("SELECT p FROM Petition p WHERE p.originalUrl = :originalUrl")
    Optional<Petition> findByUrl(@Param("originalUrl") String originUrl);

    @Query("SELECT p FROM Petition p ORDER BY p.endDate ASC")
    List<PetitionResponseDto> getEndDatePetitions(Pageable pageable);

    @Query("SELECT p FROM Petition p ORDER BY p.likesCount DESC")
    List<PetitionResponseDto> getLikesCountPetitions(Pageable pageable);

    @Query("SELECT p FROM Petition p WHERE p.category = :category ORDER BY FUNCTION('RAND')")
    List<PetitionResponseDto> getCategoryPetitionsInRandomOrder(@Param("category") Category category, Pageable pageable);

    // 제목에 검색어가 포함된 청원 조회 메서드 추가
    List<Petition> findByTitleContainingIgnoreCase(String title);

}
