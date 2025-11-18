package com.poyraz.dto;

import com.poyraz.enums.Category;
import com.poyraz.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class QuestionDTO {

    @EqualsAndHashCode.Include
    private Long id;
    @Size(max = 500, message = "{question.text.length}")
    @NotBlank(message = "{question.text.required}")
    private String questionText;
    @NotBlank(message = "{option1.required}")
    private String option1;
    @NotBlank(message = "{option2.required}")
    private String option2;
    @NotBlank(message = "{option3.required}")
    private String option3;
    private String option4;
    @NotBlank(message = "{right.answer.required}")
    private String rightAnswer;
    private DifficultyLevel difficultyLevel;
    private Category category;
}
