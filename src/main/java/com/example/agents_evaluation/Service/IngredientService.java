package com.example.agents_evaluation.Service;

import com.example.agents_evaluation.Model.Ingredient;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    @Value("${ingredient.csv.path:src/main/resources/ingredient_mappings.csv}")
    private String csvPath;

    // Патеката до фајлот со резултати од агентите
    @Value("${results.csv.path:src/main/resources/validation_results.csv}")
    private String resultsCsvPath;

    private final List<Ingredient> mappings = new ArrayList<>();

    @PostConstruct
    public void loadData() {
        // Чекор А: Го читаме првиот фајл (исто како твојот код)
        Map<String, Ingredient> ingredientMap = new LinkedHashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] header = reader.readNext();
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
                    // Го чуваме во мапа за лесно пребарување по име во следниот чекор
                    ingredientMap.put(name.toLowerCase(), ingredient);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не може да се вчита влезниот CSV: " + csvPath, e);
        }

        // Чекор Б: Го читаме вториот фајл (validation_results.csv) и ги дополнуваме податоците
        try (CSVReader reader = new CSVReader(new FileReader(resultsCsvPath))) {
            String[] header = reader.readNext();

            // Наоѓаме индекс на колоните за да не згрешиме по редослед
            List<String> headerList = Arrays.asList(header);

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length == 0) continue;

                String name = line[headerList.indexOf("ingredient")].trim();

                // Проверуваме дали оваа состојка ја имаме во нашата мапа
                Ingredient ingredient = ingredientMap.get(name.toLowerCase());

                // Ако ја нема (за секој случај), креирај ја
                if (ingredient == null) {
                    ingredient = new Ingredient(name, null, null);
                    ingredientMap.put(name.toLowerCase(), ingredient);
                }

                // Пополнување на сите нови податоци од агентите
                ingredient.setInitialAgreement(line[headerList.indexOf("initial_agreement")]);
                ingredient.setFinalAgreement(line[headerList.indexOf("final_agreement")]);
                ingredient.setDiscussionRounds(Integer.parseInt(line[headerList.indexOf("discussion_rounds")]));
                ingredient.setFinalChoice(line[headerList.indexOf("final_choice")]);
                ingredient.setConfidence(line[headerList.indexOf("confidence")]);
                ingredient.setReasoning(line[headerList.indexOf("reasoning")]);

                // Гласови на почеток
                ingredient.setNutritionistInitial(line[headerList.indexOf("nutritionist_initial")]);
                ingredient.setMedicalDoctorInitial(line[headerList.indexOf("medical_doctor_initial")]);
                ingredient.setChefInitial(line[headerList.indexOf("chef_initial")]);
                ingredient.setFoodScientistInitial(line[headerList.indexOf("food_scientist_initial")]);

                // Гласови по дискусија
                ingredient.setNutritionistFinal(line[headerList.indexOf("nutritionist_final")]);
                ingredient.setMedicalDoctorFinal(line[headerList.indexOf("medical_doctor_final")]);
                ingredient.setChefFinal(line[headerList.indexOf("chef_final")]);
                ingredient.setFoodScientistFinal(line[headerList.indexOf("food_scientist_final")]);
            }
        } catch (Exception e) {
            System.out.println("Предупредување: Не успеав да го вчитам validation_results.csv, некои полиња ќе бидат празни.");
            e.printStackTrace();
        }

        // Ги префрламе сите споени состојки во финалната листа 'mappings'
        mappings.addAll(ingredientMap.values());
    }

    public List<String> getAllIngredientNames() {
        return mappings.stream()
                .map(Ingredient::getIngredientName)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public Ingredient getOptionsByName(String name) {
        return mappings.stream()
                .filter(m -> m.getIngredientName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(new Ingredient(name, null, null));
    }
}