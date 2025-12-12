package com.poyraz.annotation.questionControllerAnnotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Delete question", description = "Delete a question from the database.")
@ApiResponse(responseCode = "204", description = "Question deleted successfully")
public @interface DeleteQuestion {
}
