package com.poyraz.service;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.exceptions.NotEnoughQuestionsException;
import com.poyraz.exceptions.QuestionNotFoundException;

import java.util.List;

public interface QuizService {
    List<QuestionDTO> createQuiz(String category, int noOfQuestions) throws QuestionNotFoundException, NotEnoughQuestionsException;
}
