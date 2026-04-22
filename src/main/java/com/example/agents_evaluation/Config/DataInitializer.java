package com.example.agents_evaluation.Config;

import com.example.agents_evaluation.Model.Ingredient;
import com.example.agents_evaluation.Repository.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(IngredientRepository repository) {
        return args -> {
            //ako ima vekje import da ne gi duplirame
            if (repository.count() == 0) {
                logger.info("Base is empty. Importing data from CSV...");

                try (BufferedReader br = new BufferedReader(new InputStreamReader(
                        new ClassPathResource("ingredient_mappings.csv").getInputStream()))) {

                    String line;
                    br.readLine(); //skokame (name, option1, option2)

                    List<Ingredient> ingredients = new ArrayList<>();

                    while ((line = br.readLine()) != null) {
                        String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                        Ingredient ingredient = new Ingredient();
                        ingredient.setIngredientName(cleanQuotes(data[1]));
                        ingredient.setOption_1(data.length > 2 ? cleanQuotes(data[2]) : "");
                        ingredient.setOption_2(data.length > 3 ? cleanQuotes(data[3]) : "");

                        ingredients.add(ingredient);
                    }

                    repository.saveAll(ingredients);
                    logger.info("Successfully imported {} ingredients.", ingredients.size());

                } catch (Exception e) {
                    logger.error("Error loading CSV: {}", e.getMessage());
                }
            } else {
                logger.info("Database already contains data, skipping CSV import.");
            }
        };
    }

    private String cleanQuotes(String value) {
        if (value == null) return "";
        return value.replace("\"", "").trim();
    }
}