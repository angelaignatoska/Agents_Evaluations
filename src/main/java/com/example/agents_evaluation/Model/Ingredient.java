package com.example.agents_evaluation.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    private String ingredientName;
    @JsonProperty("option_1")
    private String option_1;
    @JsonProperty("option_2")
    private String option_2;

    @OneToOne
    private SupremeDecision finalDecision;


    public Ingredient(String ingredientName, String option1, String option2) {
        this.ingredientName = ingredientName;
        this.option_1 = option1;
        this.option_2 = option2;
    }
}
