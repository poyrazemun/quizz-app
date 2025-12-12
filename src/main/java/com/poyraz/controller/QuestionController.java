package com.poyraz.controller;

import com.poyraz.annotation.CommonErrorResponses;
import com.poyraz.dto.ErrorDTO;
import com.poyraz.dto.QuestionDTO;
import com.poyraz.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Question", description = "APIs for creating/retrieving/deleting questions.")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;


    @Operation(summary = "Get question by question ID", description = "Retrieve question details using their unique id.")
    @ApiResponse(responseCode = "200", description = "Question Details Retrieved Successfully")
    @ApiResponse(responseCode = "404", description = "Question Not Found", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @CommonErrorResponses
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        log.info("Request: GET /question/{} - fetching question by id", id);
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        log.info("Response: question {} retrieved", id);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }

    @Operation(summary = "Get all questions", description = "Retrieve all question details from the database.")
    @ApiResponse(responseCode = "200", description = "Questions Retrieved Successfully")
    @CommonErrorResponses
    @GetMapping("/all")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        log.info("Request: GET /question/all - fetching all questions");
        List<QuestionDTO> result = questionService.getAllQuestions();
        log.info("Response: returning {} questions", result.size());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Save question", description = "Save a new question to the database.")
    @ApiResponse(responseCode = "201", description = "Question Successfully Created")
    @CommonErrorResponses
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
        log.info("Request: POST /question/addQuestion - adding question");
        String message = questionService.addQuestion(questionDTO);
        log.info("Response: question added");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Operation(summary = "Delete question", description = "Delete a new question from the database.")
    @ApiResponse(responseCode = "204", description = "Question deleted successfully")
    @ApiResponse(responseCode = "404", description = "Question Not Found", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @CommonErrorResponses
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestionById(@PathVariable @Min(value = 1, message = "{question.id.positive}") long id) {
        log.info("Request: DELETE /question/{} - deleting question", id);
        questionService.deleteQuestionById(id);
        log.info("Response: question {} deleted", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get random question", description = "Retrieve a random question from the database.")
    @ApiResponse(responseCode = "200", description = "Random Question Retrieved Successfully")
    @CommonErrorResponses
    @GetMapping("/random")
    public ResponseEntity<QuestionDTO> getRandomQuestion() {
        log.info("Request: GET /question/random - fetching random question");
        long id = 1;
        QuestionDTO questionDTO = questionService.getRandomQuestion(id);
        log.info("Response: random question retrieved");
        return ResponseEntity.status(HttpStatus.OK).body(questionDTO);
    }

    @Operation(summary = "Get questions by category", description = "Retrieve question details using their category.")
    @ApiResponse(responseCode = "200", description = "Questions Retrieved Successfully")
    @ApiResponse(responseCode = "404", description = "Category Not Exist", content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    @CommonErrorResponses
    @GetMapping("/category")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByCategory(@RequestParam String category) {
        log.info("Request: GET /question?category={} - fetching questions by category", category);
        List<QuestionDTO> questionDTOList = questionService.getQuestionByCategory(category);
        log.info("Response: returning {} questions for category {}", questionDTOList.size(), category);
        return ResponseEntity.status(HttpStatus.OK).body(questionDTOList);
    }
}
