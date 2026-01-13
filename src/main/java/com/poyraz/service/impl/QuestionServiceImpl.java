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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;
    private final KafkaTemplate<String, QuestionDTO> kafkaTemplate;

    @Value("${successfully.saved.message}")
    private String successfullySavedMessage;
    @Value("${question.not.found}")
    private String questionNotFoundMessage;
    @Value("${category.not.exist}")
    private String categoryNotExistMessage;

    @Override
    public String addQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.questionDTOToQuestion(questionDTO);
        kafkaTemplate.send("test-topic-1", "spring", questionDTO);
        questionRepository.save(question);
        return successfullySavedMessage;
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(questionMapper::questionToQuestionDTO)
                .toList();
    }

    @Override
    public QuestionDTO getQuestionById(long id) throws QuestionNotFoundException {
        return questionRepository.findById(id)
                .map(questionMapper::questionToQuestionDTO)
                .orElseThrow(() -> new QuestionNotFoundException(String.format(questionNotFoundMessage, id)));
    }

    @Override
    public void deleteQuestionById(long id) throws QuestionNotFoundException {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
        } else {
            throw new QuestionNotFoundException(String.format(questionNotFoundMessage, id));
        }
    }

    @Override
    public QuestionDTO getRandomQuestion(long ignored) {

        long count = questionRepository.count();
        if (count == 0) {
            throw new QuestionNotFoundException("No questions available");
        }
        int index = ThreadLocalRandom.current().nextInt((int) count);

        Page<Question> page = questionRepository.findAll(PageRequest.of(index, 1));
        Question question = page.getContent().getFirst();

        return questionMapper.questionToQuestionDTO(question);
    }

    @Override
    public List<QuestionDTO> getQuestionByCategory(String category) throws CategoryNotExistException {

        Category categoryEnum;
        try {
            categoryEnum = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoryNotExistException(String.format(categoryNotExistMessage, category));
        }

        return questionRepository.findByCategory(categoryEnum)
                .stream()
                .map(questionMapper::questionToQuestionDTO)
                .toList();
    }

    @KafkaListener(topics = "test-topic-2", groupId = "test-group")
    public void listener(@Payload QuestionDTO questionDTO) {
        System.out.println("Received QuestionDTO from Kafka: " + questionDTO);
    }

    @Override
    public Page<QuestionDTO> getQuestionsPage(int page, int size) {
        Page<Question> questionsPage = questionRepository.findAll(PageRequest.of(page, size));
        return questionsPage.map(questionMapper::questionToQuestionDTO);
    }
}
