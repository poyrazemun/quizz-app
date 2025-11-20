package com.poyraz.util;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.dto.QuestionWrapperDTO;
import com.poyraz.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionMapper {


    Question questionDTOToQuestion(QuestionDTO questionDTO);


    QuestionDTO questionToQuestionDTO(Question question);

    QuestionWrapperDTO questionToQuestionWrapperDTO(Question question);

}
