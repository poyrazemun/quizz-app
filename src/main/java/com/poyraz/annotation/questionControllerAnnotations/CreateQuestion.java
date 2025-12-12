package com.poyraz.annotation.questionControllerAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Save question", description = "Save a new question to the database.")
@ApiResponse(responseCode = "201", description = "Question Successfully Created")
public @interface CreateQuestion {
}
