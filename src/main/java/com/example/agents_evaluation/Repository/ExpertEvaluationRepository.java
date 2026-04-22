package com.example.agents_evaluation.Repository;

import com.example.agents_evaluation.Model.ExpertEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertEvaluationRepository extends JpaRepository<ExpertEvaluation, Long> {

    List<ExpertEvaluation> findByIngredientIdAndRoundNumber(Long ingredientId, int roundNumber);

    List<ExpertEvaluation> findByIngredientIdAndPositionChange(Long ingredientId, String positionChange);
}