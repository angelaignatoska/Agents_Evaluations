package com.example.agents_evaluation.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DiscussionRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roundNumber;
    private String agentName;
    private String message;

    public DiscussionRound() {}

    public DiscussionRound(String agentName, String message, int roundNumber) {
        this.agentName = agentName;
        this.message = message;
        this.roundNumber = roundNumber;
    }
}
