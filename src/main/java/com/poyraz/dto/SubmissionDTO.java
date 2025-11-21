package com.poyraz.dto;

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
    @NotBlank(message = "{submitter.name.required}")
    private String submitterName;

    @NotEmpty(message = "{answers.required}")
    @Valid
    private List<AnswerDTO> answers;
}

