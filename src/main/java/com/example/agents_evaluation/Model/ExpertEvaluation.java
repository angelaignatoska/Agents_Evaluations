package com.example.agents_evaluation.Model;

import com.example.agents_evaluation.Enums.ConfidenceLevel;
import com.example.agents_evaluation.Enums.EvaluationChoice;
import com.example.agents_evaluation.Enums.ExpertRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ExpertEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ingredient ingredient;

    @Enumerated(EnumType.STRING)
    private ExpertRole role;

    @Enumerated(EnumType.STRING)
    private EvaluationChoice choice;

    @Enumerated(EnumType.STRING)
    private ConfidenceLevel confidenceLevel;

    private String explanation;

    @ElementCollection
    private List<String> keyConsiderations;

    // dali agentot se predomislil vo diskusija
    private String positionChange; // MAINTAINED or CHANGED

    private int roundNumber;

}
