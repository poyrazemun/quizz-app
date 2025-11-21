package com.poyraz.dto;

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

    private Long id;
    @NotNull(message = "{question.id.required}")
    private Long questionId;

    @NotBlank(message = "{answer.required}")
    private String userAnswer;
}
