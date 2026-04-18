package com.example.agents_evaluation.Model;

import com.example.agents_evaluation.Enums.ConfidenceLevel;
import com.example.agents_evaluation.Enums.EvaluationChoice;
import jakarta.persistence.*;

import java.util.List;

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

    @Column(columnDefinition = "TEXT")
    private String reasoning;

    // kolku se soglasuvaat agentite
    private String expertAgreementLevel; // UNANIMOUS, MAJORITY, SPLIT, CONFLICTED

    // vizuelizacija na proces na diskusija
    @Column(columnDefinition = "TEXT")
    private String discussionImpact;

    // 2-3 glavni faktori koi gi iskoristil Supreme Evaluator
    @ElementCollection
    private List<String> keyFactors;

    @Column(columnDefinition = "TEXT")
    private String rawMetaData;

}
