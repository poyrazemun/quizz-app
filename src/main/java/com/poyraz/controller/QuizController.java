package com.poyraz.controller;

import com.poyraz.dto.QuestionDTO;
import com.poyraz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<List<QuestionDTO>> createQuiz(@RequestParam String category, @RequestParam int noOfQuestions) {
        log.info("Request: POST /quiz/create - creating quiz with category {} and {} questions", category, noOfQuestions);
        List<QuestionDTO> result = quizService.createQuiz(category, noOfQuestions);
        log.info("Response: question added");
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
