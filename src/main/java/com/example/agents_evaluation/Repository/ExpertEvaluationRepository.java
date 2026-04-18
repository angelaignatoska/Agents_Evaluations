package com.example.agents_evaluation.Repository;

import com.example.agents_evaluation.Model.ExpertEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertEvaluationRepository extends JpaRepository<ExpertEvaluation, Long> {
    Optional<ExpertEvaluation> findById(Long id); //gi zema site mislenja za konkretna sostojka

    List<ExpertEvaluation> fingByIdAndisInitial(Long id, String positionChanged); //gi filtrira finalnite

}
