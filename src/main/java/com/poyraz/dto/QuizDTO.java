package com.poyraz.dto;

import com.poyraz.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO {
    private Long id;
    private String quizName;
    private Category category;
    private int questionCount;
}

