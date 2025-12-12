package com.poyraz.annotation.quizControllerAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Submit the quiz with the answers", description = "Submit the quiz answers for evaluation using the quiz id.")
@ApiResponse(responseCode = "200", description = "Quiz submitted Successfully")
public @interface SubmitQuiz {
}
