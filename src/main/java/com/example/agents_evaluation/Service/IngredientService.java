package com.example.agents_evaluation.Service;

import com.example.agents_evaluation.Model.Ingredient;
import com.opencsv.CSVReader;
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

    private final List<Ingredient> mappings = new ArrayList<>();

    @PostConstruct
    public void loadCsv() {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            String[] header = reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {

                if (line.length < 2) continue;

                String name = line.length > 1 ? line[1].trim() : "";
                String option1 = line.length > 2 ? line[2].trim() : "";
                String option2 = line.length > 3 ? line[3].trim() : "";

                if (!name.isEmpty()) {
                    mappings.add(new Ingredient(
                            name,
                            option1.isEmpty() ? null : option1,
                            option2.isEmpty() ? null : option2
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Не може да се вчита CSV: " + csvPath, e);
        }
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