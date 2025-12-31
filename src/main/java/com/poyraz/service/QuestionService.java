package com.poyraz.service;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.exceptions.CategoryNotExistException;
import com.poyraz.exceptions.QuestionNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuestionService {

    String addQuestion(QuestionDTO questionDTO);

    List<QuestionDTO> getAllQuestions();

    QuestionDTO getQuestionById(long id) throws QuestionNotFoundException;

    void deleteQuestionById(long id) throws QuestionNotFoundException;

    QuestionDTO getRandomQuestion(long id);

    List<QuestionDTO> getQuestionByCategory(String category) throws CategoryNotExistException;
    
    Page<QuestionDTO> getQuestionsPage(int page, int size);
}
