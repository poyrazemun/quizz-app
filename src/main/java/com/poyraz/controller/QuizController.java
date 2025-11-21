package com.poyraz.controller;

import com.poyraz.dto.QuestionWrapperDTO;
import com.poyraz.dto.SubmissionDTO;
import com.poyraz.dto.SubmissionResultDTO;
import com.poyraz.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam @Positive(message = "{quiz.questionCount.positive}") int noOfQuestions, @RequestParam @NotBlank(message = "{quiz.name.required}") String quizName) {
        String message = quizService.createQuiz(category, noOfQuestions, quizName);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<QuestionWrapperDTO>> getQuizQuestionsById(@PathVariable @Min(value = 1, message = "{quiz.id.positive}") long id) {
        List<QuestionWrapperDTO> questions = quizService.getQuizQuestionsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<SubmissionResultDTO> submitQuiz(@PathVariable @Min(value = 1, message = "{quiz.id.positive}") long id, @RequestBody @Valid SubmissionDTO submissionDTO) {
        log.info("Request: POST /quiz/submit - submitting quiz answers for quiz id {} by {}", id, submissionDTO.getSubmitterName());
        SubmissionResultDTO result = quizService.submitQuiz(id, submissionDTO);
        log.info("Response: quiz answers submitted for id {} by {} -> correct: {} wrong: {}", id, submissionDTO.getSubmitterName(), result.getCorrectAnswers(), result.getWrongAnswers());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
