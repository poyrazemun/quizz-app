package com.poyraz.service.impl;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.dto.QuestionWrapperDTO;
import com.poyraz.entity.Quiz;
import com.poyraz.enums.Category;
import com.poyraz.exceptions.CategoryNotExistException;
import com.poyraz.exceptions.NotEnoughQuestionsException;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.exceptions.QuizNotFoundException;
import com.poyraz.repository.QuestionRepository;
import com.poyraz.repository.QuizRepository;
import com.poyraz.service.QuizService;
import com.poyraz.util.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Value("${quiz.creation.message}")
    private String quizCreationMessage;
    @Value("${quiz.not.found}")
    private String quizNotFoundMessage;

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;


    @Override
    public String createQuiz(String category, int noOfQuestions, String quizName) {
        Quiz quiz = Quiz.builder().quizName(quizName).category(Category.valueOf(category.toUpperCase()))
                .questionCount(noOfQuestions).createdAt(LocalDateTime.now()).build();
        List<QuestionDTO> list = createQuestionsOfTheQuiz(category, noOfQuestions);
        quiz.setQuestions(list.stream().map(questionMapper::questionDTOToQuestion).toList());
        quizRepository.save(quiz);
        log.info("Quiz created with name: {}, category: {}, question count: {}", quizName, category, noOfQuestions);
        return String.format(quizCreationMessage, quiz.getId());

    }

    @Override
    public List<QuestionWrapperDTO> getQuizQuestionsById(long id) throws QuizNotFoundException {
        if (quizRepository.existsById(id)) {
            Quiz quiz = quizRepository.findById(id).orElseThrow();
            log.info("Retrieving questions for quiz id: {}", id);
            return quiz.getQuestions().stream().map(questionMapper::questionToQuestionWrapperDTO).toList();
        } else {
            throw new QuizNotFoundException(String.format(quizNotFoundMessage, id));
        }
    }


    public List<QuestionDTO> createQuestionsOfTheQuiz(String category, int noOfQuestions) throws QuestionNotFoundException, NotEnoughQuestionsException {
        Category cat;
        try {
            cat = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotExistException(String.format(categoryNotExistMessage, category));
        }
        List<QuestionDTO> list = new java.util.ArrayList<>(questionRepository.findByCategory(cat)
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
