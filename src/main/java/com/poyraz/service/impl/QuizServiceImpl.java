package com.poyraz.service.impl;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.enums.Category;
import com.poyraz.exceptions.CategoryNotExistException;
import com.poyraz.exceptions.NotEnoughQuestionsException;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.repository.QuizRepository;
import com.poyraz.service.QuizService;
import com.poyraz.util.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

    @Value("${category.not.exist}")
    private String categoryNotExistMessage;
    @Value("${not.enough.questions}")
    private String notEnoughQuestionsMessage;

    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper;


    @Override
    public List<QuestionDTO> createQuiz(String category, int noOfQuestions) throws QuestionNotFoundException, NotEnoughQuestionsException {
        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotExistException(String.format(categoryNotExistMessage, category));
        }
        log.info("Service: retrieving random {} questions for category {}", noOfQuestions, cat);
        List<QuestionDTO> list = new java.util.ArrayList<>(quizRepository.findByCategory(cat)
                .stream()
                .map(questionMapper::questionToQuestionDTO)
                .toList());

        if (list.size() < noOfQuestions) {
            throw new NotEnoughQuestionsException(String.format(notEnoughQuestionsMessage, category, noOfQuestions, list.size()));
        }
        Collections.shuffle(list);

        return list.subList(0, noOfQuestions);

    }
}
