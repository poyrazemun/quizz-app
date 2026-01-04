package com.poyraz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizFormDTO {

    @NotBlank(message = "{quiz.name.required}")
    private String quizName;

    @NotBlank(message = "{category.not.blank}")
    private String category;

    @Positive(message = "{quiz.questionCount.positive}")
    private int noOfQuestions;
}

