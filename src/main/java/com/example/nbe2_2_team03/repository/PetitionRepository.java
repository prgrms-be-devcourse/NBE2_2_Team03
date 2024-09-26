package com.example.nbe2_2_team03.repository;

import com.example.nbe2_2_team03.entity.Petition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetitionRepository extends JpaRepository<Petition, Long> {
}
