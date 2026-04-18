package com.example.agents_evaluation.Controller;

import com.example.agents_evaluation.Model.Ingredient;
import com.example.agents_evaluation.Repository.IngredientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class EvaluationController {

    private final IngredientRepository ingredientRepository;

    public EvaluationController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    // Dashboard showing all ingredients and their final consensus
    @GetMapping("/")
    public String showDashboard(Model model) {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        model.addAttribute("ingredients", ingredients);
        return "dashboard";
    }

    // Detailed view of the multi-agent process for one specific ingredient
    @GetMapping("/ingredient/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));

        model.addAttribute("ingredient", ingredient);
        // This will include the SupremeDecision and all ExpertEvaluations via JPA relationships
        return "details";
    }
}