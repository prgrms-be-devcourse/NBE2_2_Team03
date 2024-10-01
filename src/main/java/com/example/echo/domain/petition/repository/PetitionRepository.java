package com.example.echo.domain.petition.repository;

import com.example.echo.domain.petition.entity.Petition;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetitionRepository extends JpaRepository<Petition, Long> {

    @Query("SELECT p FROM Petition p WHERE p.originalUrl = :originalUrl")
    Optional<Petition> findByUrl(@Param("originalUrl") String originUrl);
}
