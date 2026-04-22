package com.example.agents_evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AgentsEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentsEvaluationApplication.class, args);
    }

}
