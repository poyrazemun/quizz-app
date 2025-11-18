package com.poyraz.controller;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.service.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        String message = questionService.addQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        questionService.deleteQuestionById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
