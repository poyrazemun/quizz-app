package com.poyraz.controller;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.service.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        log.info("Request: GET /question/{} - fetching question by id", id);
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        log.info("Response: question {} retrieved", id);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        log.info("Request: GET /question/all - fetching all questions");
        List<QuestionDTO> result = questionService.getAllQuestions();
        log.info("Response: returning {} questions", result.size());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        log.info("Request: POST /question/addQuestion - adding question");
        String message = questionService.addQuestion(questionDTO);
        log.info("Response: question added");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        log.info("Request: DELETE /question/{} - deleting question", id);
        questionService.deleteQuestionById(id);
        log.info("Response: question {} deleted", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/random")
    public ResponseEntity<QuestionDTO> getRandomQuestion() {
        log.info("Request: GET /question/random - fetching random question");
        long id = 1;
        QuestionDTO questionDTO = questionService.getRandomQuestion(id);
        log.info("Response: random question retrieved");
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }
}
