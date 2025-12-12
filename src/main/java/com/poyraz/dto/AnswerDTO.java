package com.poyraz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class AnswerDTO {

    @EqualsAndHashCode.Include
    @Schema(description = "Unique ID of the answer record")
    private Long id;
    @Schema(description = "ID of the question being answered")
    @NotNull(message = "{question.id.required}")
    private Long questionId;

    @Schema(description = "User's answer to the question")
    @NotBlank(message = "{answer.required}")
    private String userAnswer;
}
