package com.poyraz.dto;

import com.poyraz.enums.Category;
import com.poyraz.enums.DifficultyLevel;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Unique ID of the question")
    @EqualsAndHashCode.Include
    private Long id;
    @Schema(description = "Text of the question")
    @Size(max = 500, message = "{question.text.length}")
    @NotBlank(message = "{question.text.required}")
    private String questionText;
    @Schema(description = "First option for the question")
    @NotBlank(message = "{option1.required}")
    private String option1;
    @Schema(description = "Second option for the question")
    @NotBlank(message = "{option2.required}")
    private String option2;
    @Schema(description = "Third option for the question")
    @NotBlank(message = "{option3.required}")
    private String option3;
    @Schema(description = "Fourth option for the question", nullable = true)
    private String option4;
    @Schema(description = "Correct answer for the question")
    @NotBlank(message = "{right.answer.required}")
    private String rightAnswer;
    @Schema(description = "Difficulty level of the question")
    private DifficultyLevel difficultyLevel;
    @Schema(description = "Category of the question")
    private Category category;
}
