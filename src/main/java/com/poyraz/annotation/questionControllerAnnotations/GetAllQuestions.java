package com.poyraz.annotation.questionControllerAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Get all questions", description = "Retrieve all question details from the database.")
@ApiResponse(responseCode = "200", description = "Questions Retrieved Successfully")
public @interface GetAllQuestions {
}
