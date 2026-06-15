package com.example.agents_evaluation.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private String initialAgreement;
    private String finalAgreement;
    private int discussionRounds;
    private String finalChoice;
    private String confidence;
    private String reasoning;

    private String nutritionistInitial;
    private String medicalDoctorInitial;
    private String chefInitial;
    private String foodScientistInitial;

    private String nutritionistFinal;
    private String medicalDoctorFinal;
    private String chefFinal;
    private String foodScientistFinal;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DiscussionRound> discussionHistory = new ArrayList<>();

    public Ingredient(String ingredientName, String option1, String option2) {
        this.ingredientName = ingredientName;
        this.option_1 = option1;
        this.option_2 = option2;
    }


}
