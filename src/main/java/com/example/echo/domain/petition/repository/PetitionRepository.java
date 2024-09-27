package com.example.echo.domain.petition.repository;

import com.example.echo.domain.petition.entity.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
}
