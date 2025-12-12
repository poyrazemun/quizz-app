package com.poyraz.annotation.quizControllerAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Create Quiz", description = "Create a new quiz with specified category, number of questions, and quiz name.")
@ApiResponse(responseCode = "201", description = "Quiz Successfully Created", content = @Content(schema = @Schema(implementation = String.class)))
public @interface CreateQuiz {
}
