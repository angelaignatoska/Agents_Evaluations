package com.example.agents_evaluation.Model;

import com.example.agents_evaluation.Enums.ConfidenceLevel;
import com.example.agents_evaluation.Enums.EvaluationChoice;
import jakarta.persistence.*;

@Entity
public class SupremeDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ingredient ingredient;

    @Enumerated(EnumType.STRING)
    private EvaluationChoice finalChoice;

    @Enumerated(EnumType.STRING)
    private ConfidenceLevel confidenceLevel;

    private String reasoning;

    private String rawMetaData;
}
