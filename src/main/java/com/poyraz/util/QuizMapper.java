package com.poyraz.util;

import com.poyraz.dto.QuizDTO;
import com.poyraz.entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuizMapper {
    QuizDTO quizToQuizDTO(Quiz quiz);
}
