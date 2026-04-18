package com.example.agents_evaluation.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ingredientName;
    private String option1;
    private String option2;

    @OneToOne(mappedBy = "mapping", cascade = CascadeType.ALL)
    private SupremeDecision finalDecision;

}
