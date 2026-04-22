package com.example.agents_evaluation.Web;

import com.example.agents_evaluation.Model.Ingredient;
import com.example.agents_evaluation.Repository.IngredientRepository;
import com.example.agents_evaluation.Service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"/ingredients","/"})
public class EvaluationController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    public String showIngredients(Model model){
        List<String> ingredientNames = ingredientService.getAllIngredientNames();
        model.addAttribute("ingredientNames", ingredientNames);
        return "ingredients";
    }

    @GetMapping("/options")
    @ResponseBody
    public Ingredient getOptions(@RequestParam("name") String name) {
        return ingredientService.getOptionsByName(name);
    }
}