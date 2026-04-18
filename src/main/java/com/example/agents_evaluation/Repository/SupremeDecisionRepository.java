package com.example.agents_evaluation.Repository;

import com.example.agents_evaluation.Model.SupremeDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupremeDecisionRepository extends JpaRepository<SupremeDecision, Long> {

    @Override
    Optional<SupremeDecision> findById(Long aLong);
}
