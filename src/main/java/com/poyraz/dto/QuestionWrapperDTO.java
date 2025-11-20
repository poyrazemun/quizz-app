package com.poyraz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
public class QuestionWrapperDTO {
    @EqualsAndHashCode.Include
    private Long id;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

}
