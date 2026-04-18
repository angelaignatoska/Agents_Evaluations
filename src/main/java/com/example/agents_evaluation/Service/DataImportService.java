package com.example.agents_evaluation.Service;

import com.example.agents_evaluation.Enums.*;
import com.example.agents_evaluation.Model.*;
import com.example.agents_evaluation.Repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportService {

    private final IngredientRepository ingredientRepository;
    private final ObjectMapper objectMapper;

    public DataImportService(IngredientRepository ingredientRepository, ObjectMapper objectMapper) {
        this.ingredientRepository = ingredientRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void importFromJson(String filePath) throws IOException {
        JsonNode root = objectMapper.readTree(new File(filePath));

        for (JsonNode node : root) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientName(node.get("ingredient").asText());
            ingredient.setOption1(node.get("option_1").asText());
            ingredient.setOption2(node.get("option_2").asText());

            List<ExpertEvaluation> evaluations = new ArrayList<>();

            // 1. Process Initial Evaluations (Round 0)
            parseEvaluations(node.get("initial_evaluations"), ingredient, 0, evaluations);

            // 2. Process Final Evaluations (Round 1 or Last Round)
            parseEvaluations(node.get("final_evaluations"), ingredient, 1, evaluations);

            // 3. Process Supreme Decision
            JsonNode supremeNode = node.get("supreme_decision");
            SupremeDecision supreme = new SupremeDecision();
            supreme.setIngredient(ingredient);
            supreme.setFinalChoice(EvaluationChoice.valueOf(supremeNode.get("final_choice").asText()));
            supreme.setConfidenceLevel(ConfidenceLevel.valueOf(supremeNode.get("confidence").asText().toUpperCase()));
            supreme.setReasoning(supremeNode.get("reasoning").asText());
            supreme.setExpertAgreementLevel(supremeNode.get("expert_agreement_level").asText());

            List<String> factors = new ArrayList<>();
            supremeNode.get("key_factors").forEach(f -> factors.add(f.asText()));
            supreme.setKeyFactors(factors);

            ingredient.setFinalDecision(supreme);
            ingredientRepository.save(ingredient);
        }
    }

    private void parseEvaluations(JsonNode evaluationsNode, Ingredient ingredient, int round, List<ExpertEvaluation> list) {
        for (JsonNode evalNode : evaluationsNode) {
            ExpertEvaluation eval = new ExpertEvaluation();
            eval.setIngredient(ingredient);
            eval.setRoundNumber(round);
            eval.setRole(ExpertRole.valueOf(evalNode.get("expert_role").asText().toUpperCase().replace(" ", "_")));
            eval.setChoice(EvaluationChoice.valueOf(evalNode.get("choice").asText()));
            eval.setConfidenceLevel(ConfidenceLevel.valueOf(evalNode.get("confidence").asText().toUpperCase()));
            eval.setExplanation(evalNode.get("explanation").asText());

            List<String> considerations = new ArrayList<>();
            evalNode.get("key_considerations").forEach(c -> considerations.add(c.asText()));
            eval.setKeyConsiderations(considerations);

            list.add(eval);
        }
    }
}