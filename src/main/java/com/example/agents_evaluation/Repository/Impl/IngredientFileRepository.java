package com.example.agents_evaluation.Repository.Impl;

import com.example.agents_evaluation.Model.DiscussionRound;
import com.example.agents_evaluation.Model.Ingredient;
import com.example.agents_evaluation.Repository.FileReader;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

@Repository
public class IngredientFileRepository implements FileReader {

    @Value("${ingredient.csv.path:src/main/resources/ingredient_mappings.csv}")
    private String csvPath;

    @Value("${results.csv.path:src/main/resources/validation_results.csv}")
    private String resultsCsvPath;

    @Value("${results.json.path:src/main/resources/validation_results_detailed.json}")
    private String jsonPath;


    @Override
    public List<Ingredient> findAll() {
        Map<String, Ingredient> ingredientMap = new LinkedHashMap<>();

        try (CSVReader reader = new CSVReader(new java.io.FileReader(csvPath))) {
            reader.readNext();
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 2) continue;

                String name = line.length > 1 ? line[1].trim() : "";
                String option1 = line.length > 2 ? line[2].trim() : "";
                String option2 = line.length > 3 ? line[3].trim() : "";

                if (!name.isEmpty()) {
                    Ingredient ingredient = new Ingredient(
                            name,
                            option1.isEmpty() ? null : option1,
                            option2.isEmpty() ? null : option2
                    );
                    ingredientMap.put(name.toLowerCase(), ingredient);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid CSV: " + csvPath, e);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(jsonPath);

            if (jsonFile.exists()) {
                JsonNode rootNode = mapper.readTree(jsonFile);

                JsonNode items = rootNode.isArray() ? rootNode : rootNode.get("result");

                if (items != null) {
                    for (JsonNode node : items) {
                        // Внатре во циклусот for (JsonNode node : items)
                        String name = node.get("ingredient").asText().trim();
                        Ingredient ingredient = ingredientMap.getOrDefault(name.toLowerCase(), new Ingredient(name, null, null));

// 1. Пополни Supreme Decision
                        if (node.has("supreme_decision")) {
                            JsonNode supreme = node.get("supreme_decision");
                            ingredient.setFinalChoice(supreme.path("final_choice").asText("Undecided"));
                            ingredient.setReasoning(supreme.path("reasoning").asText(""));
                            ingredient.setConfidence(supreme.path("confidence").asText("N/A"));
                        }

// 2. Пополни Final Agreement (од final_consensus)
                        if (node.has("final_consensus")) {
                            ingredient.setFinalAgreement(node.get("final_consensus").path("agreement_level").asText("Split"));
                        }

// 3. Дискусија - ова е многу важно!
// Според твојот JSON, discussion_history е листа од листи (бидејќи имаш загради [ [ ... ] ])
                        List<DiscussionRound> history = new ArrayList<>();
                        int roundCounter = 1; // Почни од 1

                        if (node.has("discussion_history") && node.get("discussion_history").isArray()) {
                            for (JsonNode roundGroup : node.get("discussion_history")) {
                                // Ова е една група на дискусија
                                for (JsonNode roundNode : roundGroup) {
                                    String agent = roundNode.path("expert_role").asText("Unknown");
                                    String message = roundNode.path("response_to_colleagues").asText(roundNode.path("explanation").asText(""));

                                    // Користи го roundCounter наместо 0
                                    history.add(new DiscussionRound(agent, message, roundCounter));
                                }
                                roundCounter++; // Зголеми ја рундата откако ќе ја завршиш групата
                            }
                        }
                        ingredient.setDiscussionHistory(history);
                        ingredientMap.put(name.toLowerCase(), ingredient);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading JSON" + e.getMessage());
            e.printStackTrace();
        }

        try (CSVReader reader = new CSVReader(new java.io.FileReader(resultsCsvPath))) {
            String[] header = reader.readNext();

            List<String> headerList = Arrays.asList(header);

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 0) continue;

                String name = line[headerList.indexOf("ingredient")].trim();

                Ingredient ingredient = ingredientMap.get(name.toLowerCase());

                if (ingredient == null) {
                    ingredient = new Ingredient(name, null, null);
                    ingredientMap.put(name.toLowerCase(), ingredient);
                }


                ingredient.setInitialAgreement(line[headerList.indexOf("initial_agreement")]);
                ingredient.setFinalAgreement(line[headerList.indexOf("final_agreement")]);
                ingredient.setDiscussionRounds(Integer.parseInt(line[headerList.indexOf("discussion_rounds")]));
                ingredient.setFinalChoice(line[headerList.indexOf("final_choice")]);
                ingredient.setConfidence(line[headerList.indexOf("confidence")]);
                ingredient.setReasoning(line[headerList.indexOf("reasoning")]);


                ingredient.setNutritionistInitial(line[headerList.indexOf("nutritionist_initial")]);
                ingredient.setMedicalDoctorInitial(line[headerList.indexOf("medical_doctor_initial")]);
                ingredient.setChefInitial(line[headerList.indexOf("chef_initial")]);
                ingredient.setFoodScientistInitial(line[headerList.indexOf("food_scientist_initial")]);


                ingredient.setNutritionistFinal(line[headerList.indexOf("nutritionist_final")]);
                ingredient.setMedicalDoctorFinal(line[headerList.indexOf("medical_doctor_final")]);
                ingredient.setChefFinal(line[headerList.indexOf("chef_final")]);
                ingredient.setFoodScientistFinal(line[headerList.indexOf("food_scientist_final")]);
            }
        } catch (Exception e) {
            System.out.println("Invalid CSV");
            e.printStackTrace();
        }

        return new ArrayList<>(ingredientMap.values());
    }
}
