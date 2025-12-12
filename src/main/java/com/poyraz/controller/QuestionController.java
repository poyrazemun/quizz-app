package com.poyraz.controller;

import com.poyraz.annotation.CommonErrorResponses;
import com.poyraz.annotation.questionControllerAnnotations.*;
import com.poyraz.dto.ErrorDTO;
import com.poyraz.dto.QuestionDTO;
import com.poyraz.service.QuestionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Question", description = "APIs for creating/retrieving/deleting questions.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;


    @GetQuestion
    @ApiResponse(responseCode = "404", description = "Question Not Found", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @CommonErrorResponses
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }

    @GetAllQuestions
    @CommonErrorResponses
    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> result = questionService.getAllQuestions();
        return ResponseEntity.ok(result);
    }

    @CreateQuestion
    @CommonErrorResponses
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        String message = questionService.addQuestion(questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @DeleteQuestion
    @ApiResponse(responseCode = "404", description = "Question Not Found", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @CommonErrorResponses
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        questionService.deleteQuestionById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetRandomQuestion
    @CommonErrorResponses
    @GetMapping("/random")
    public ResponseEntity<QuestionDTO> getRandomQuestion() {
        long id = 1;
        QuestionDTO questionDTO = questionService.getRandomQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }

    @GetQuestionsByCategory
    @ApiResponse(responseCode = "404", description = "Category Not Exist", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @CommonErrorResponses
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getQuestionsByCategory(@RequestParam String category) {
        List<QuestionDTO> questionDTOList = questionService.getQuestionByCategory(category);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTOList);
    }
}
