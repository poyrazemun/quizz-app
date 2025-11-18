package com.poyraz.service.impl;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.entity.Question;
import com.poyraz.exceptions.QuestionNotFoundException;
import com.poyraz.repository.QuestionRepository;
import com.poyraz.service.QuestionService;
import com.poyraz.util.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionRepository questionRepository;

    @Value("${successfully.saved.message}")
    private String successfullySavedMessage;
    @Value("${question.not.found}")
    private String questionNotFoundMessage;

    @Override
    public String addQuestion(QuestionDTO questionDTO) {
        Question question = questionMapper.questionDTOToQuestion(questionDTO);
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
        if (questionRepository.findById(id).isPresent()) {
            questionRepository.deleteById(id);
        } else {
            throw new QuestionNotFoundException(String.format(questionNotFoundMessage, id));
        }
    }
}
