package com.poyraz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmissionResultDTO {
    private int correctAnswers;
    private int wrongAnswers;
    private int totalQuestions;
    private double percentage;
}

