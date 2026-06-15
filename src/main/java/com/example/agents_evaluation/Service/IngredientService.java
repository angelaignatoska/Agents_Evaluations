package com.example.agents_evaluation.Service;

import com.example.agents_evaluation.Model.Ingredient;
import com.example.agents_evaluation.Repository.Impl.IngredientFileRepository;
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

    private final IngredientFileRepository ingredientFileRepository;

    public IngredientService(IngredientFileRepository ingredientFileRepository) {
        this.ingredientFileRepository = ingredientFileRepository;
    }

    public List<String> getAllIngredientNames() {
        return ingredientFileRepository.findAll().stream()
                .map(Ingredient::getIngredientName)
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    public Ingredient getOptionsByName(String name) {
        return ingredientFileRepository.findAll().stream()
                .filter(m -> m.getIngredientName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(new Ingredient(name, null, null));
    }
}