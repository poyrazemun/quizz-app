package com.poyraz.annotation.quizControllerAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Get quiz by quiz ID", description = "Retrieve quiz and its questions using the id of the quiz.")
@ApiResponse(responseCode = "200", description = "Quiz retrieved Successfully")
public @interface GetQuiz {
}
