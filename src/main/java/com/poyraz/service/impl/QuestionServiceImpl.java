package com.poyraz.service.impl;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.entity.Question;
import com.poyraz.enums.Category;
import com.poyraz.exceptions.CategoryNotExistException;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.repository.QuestionRepository;
import com.poyraz.service.QuestionService;
import com.poyraz.util.QuestionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;

    @Value("${successfully.saved.message}")
    private String successfullySavedMessage;
    @Value("${question.not.found}")
    private String questionNotFoundMessage;
    @Value("${category.not.exist}")
    private String categoryNotExistMessage;

    @Override
    public String addQuestion(QuestionDTO questionDTO) {
        log.info("Service: addQuestion - saving question");
        Question question = questionMapper.questionDTOToQuestion(questionDTO);
        questionRepository.save(question);
        log.info("Service: addQuestion - question saved");
        return successfullySavedMessage;
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        log.info("Service: getAllQuestions - retrieving all questions");
        List<QuestionDTO> list = questionRepository.findAll()
                .stream()
                .map(questionMapper::questionToQuestionDTO)
                .toList();
        log.info("Service: getAllQuestions - retrieved {} questions", list.size());
        return list;
    }

    @Override
    public QuestionDTO getQuestionById(long id) throws QuestionNotFoundException {
        log.info("Service: getQuestionById - fetching id {}", id);
        return questionRepository.findById(id)
                .map(q -> {
                    log.info("Service: getQuestionById - found id {}", id);
                    return questionMapper.questionToQuestionDTO(q);
                })
                .orElseThrow(() -> {
                    log.warn("Service: getQuestionById - not found id {}", id);
                    return new QuestionNotFoundException(String.format(questionNotFoundMessage, id));
                });
    }

    @Override
    public void deleteQuestionById(long id) throws QuestionNotFoundException {
        log.info("Service: deleteQuestionById - deleting id {}", id);
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            log.info("Service: deleteQuestionById - deleted id {}", id);
        } else {
            log.warn("Service: deleteQuestionById - not found id {}", id);
            throw new QuestionNotFoundException(String.format(questionNotFoundMessage, id));
        }
    }

    @Override
    public QuestionDTO getRandomQuestion(long ignored) {
        log.info("Service: getRandomQuestion - selecting random question");
        long count = questionRepository.count();
        if (count == 0) {
            log.warn("Service: getRandomQuestion - no questions available");
            throw new QuestionNotFoundException("No questions available");
        }
        int index = ThreadLocalRandom.current().nextInt((int) count);
        log.info("Service: getRandomQuestion - selected index {}", index);
        Page<Question> page = questionRepository.findAll(PageRequest.of(index, 1));
        Question question = page.getContent().getFirst();
        log.info("Service: getRandomQuestion - returning random question");
        return questionMapper.questionToQuestionDTO(question);
    }

    @Override
    public List<QuestionDTO> getQuestionByCategory(String category) throws CategoryNotExistException {

        log.info("Service: getQuestionByCategory - retrieving questions with category {}", category);
        Category categoryEnum;
        try {
            categoryEnum = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotExistException(String.format(categoryNotExistMessage, category));
        }

        List<QuestionDTO> list = questionRepository.findByCategory(categoryEnum)
                .stream()
                .map(questionMapper::questionToQuestionDTO)
                .toList();

        log.info("Service: getQuestionByCategory - retrieved {} questions with category {}", list.size(), category);
        return list;
    }
}
