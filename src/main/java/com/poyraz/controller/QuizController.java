package com.poyraz.controller;

import com.poyraz.annotation.CommonErrorResponses;
import com.poyraz.annotation.quizControllerAnnotations.CreateQuiz;
import com.poyraz.annotation.quizControllerAnnotations.GetQuiz;
import com.poyraz.annotation.quizControllerAnnotations.SubmitQuiz;
import com.poyraz.dto.ErrorDTO;
import com.poyraz.dto.QuestionWrapperDTO;
import com.poyraz.dto.SubmissionDTO;
import com.poyraz.dto.SubmissionResultDTO;
import com.poyraz.service.QuizService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Quiz", description = "APIs for creating/retrieving/submitting quizzes.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;


    @CreateQuiz
    @CommonErrorResponses
    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestParam String category,
                                             @RequestParam @Positive(message = "{quiz.questionCount.positive}") int noOfQuestions,
                                             @RequestParam @NotBlank(message = "{quiz.name.required}") String quizName) {
        String message = quizService.createQuiz(category, noOfQuestions, quizName);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }


    @GetQuiz
    @ApiResponse(responseCode = "404", description = "Quiz Not Found", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @GetMapping("/{id}")
    public ResponseEntity<List<QuestionWrapperDTO>> getQuizQuestionsById(@PathVariable @Min(value = 1, message = "{quiz.id.positive}") long id) {
        List<QuestionWrapperDTO> questions = quizService.getQuizQuestionsById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questions);
    }

    @SubmitQuiz
    @ApiResponse(responseCode = "404", description = "Quiz Not Found", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @PostMapping("submit/{id}")
    public ResponseEntity<SubmissionResultDTO> submitQuiz(@PathVariable @Min(value = 1, message = "{quiz.id.positive}") long id, @RequestBody @Valid SubmissionDTO submissionDTO) {
        SubmissionResultDTO result = quizService.submitQuiz(id, submissionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
