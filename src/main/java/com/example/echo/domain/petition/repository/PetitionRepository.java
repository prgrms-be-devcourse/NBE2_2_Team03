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
    Page<Petition> findByCategory(Category category, Pageable pageable);

    @Query("SELECT p FROM Petition p WHERE p.originalUrl = :originalUrl")
    Optional<Petition> findByUrl(@Param("originalUrl") String originUrl);

    @Query("SELECT p FROM Petition p ORDER BY p.endDate ASC")
    List<PetitionResponseDto> getEndDatePetitions(Pageable pageable);

    @Query("SELECT p FROM Petition p ORDER BY p.agreeCount DESC")
    List<PetitionResponseDto> getAgreeCountPetitions(Pageable pageable);
}
