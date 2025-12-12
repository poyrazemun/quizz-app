package com.poyraz.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmissionDTO {
    @Schema(description = "Name of the person submitting the quiz")
    @NotBlank(message = "{submitter.name.required}")
    private String submitterName;

    @Schema(description = "List of answers provided for the quiz")
    @NotEmpty(message = "{answers.required}")
    @Valid
    private List<AnswerDTO> answers;
}

