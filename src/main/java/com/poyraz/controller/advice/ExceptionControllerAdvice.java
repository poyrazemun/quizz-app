package com.poyraz.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.poyraz.dto.ErrorDTO;
import com.poyraz.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleQuestionNotFoundException(QuestionNotFoundException questionNotFoundException, HttpServletRequest request) {
        log.warn("QuestionNotFoundException at URI={} - message={}",
                request.getRequestURI(),
                questionNotFoundException.getMessage()
        );
        ErrorDTO errorDTO = buildErrorDTO(questionNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(errorDTO.getError()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleQuizNotFoundException(QuizNotFoundException quizNotFoundException, HttpServletRequest request) {
        log.warn("QuizNotFoundException at URI={} - message={}",
                request.getRequestURI(),
                quizNotFoundException.getMessage()
        );
        ErrorDTO errorDTO = buildErrorDTO(quizNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(errorDTO.getError()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleNotEnoughQuestionsException(NotEnoughQuestionsException notEnoughQuestionsException, HttpServletRequest request) {
        log.warn("NotEnoughQuestionsException at URI={} - message={}",
                request.getRequestURI(),
                notEnoughQuestionsException.getMessage()
        );
        ErrorDTO errorDTO = buildErrorDTO(notEnoughQuestionsException.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(errorDTO.getError()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleCategoryNotExistException(CategoryNotExistException categoryNotExistException, HttpServletRequest request) {
        log.warn("CategoryNotExistException at URI={} - message={}",
                request.getRequestURI(),
                categoryNotExistException.getMessage()
        );
        ErrorDTO errorDTO = buildErrorDTO(categoryNotExistException.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(errorDTO.getError()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUsernameAlreadyExistException(UsernameAlreadyExistsException usernameAlreadyExistsException, HttpServletRequest request) {
        log.warn("UsernameAlreadyExistsException at URI={} - message={}",
                request.getRequestURI(),
                usernameAlreadyExistsException.getMessage()
        );
        ErrorDTO errorDTO = buildErrorDTO(usernameAlreadyExistsException.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(errorDTO.getError()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorDTO>> handleConstraintViolationException(ConstraintViolationException exception) {
        log.warn("Handling ConstraintViolationException: {} violations", exception.getConstraintViolations().size());
        List<ErrorDTO> errorDTOList = exception.getConstraintViolations()
                .stream()
                .map(error -> buildErrorDTO(error.getMessage(), HttpStatus.BAD_REQUEST)
                )
                .toList();
        return ResponseEntity.status(errorDTOList.getFirst().getStatus()).body(errorDTOList);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        log.warn("Handling HttpRequestMethodNotSupportedException: {}", httpRequestMethodNotSupportedException.getMessage());
        ErrorDTO errorDTO = buildErrorDTO(httpRequestMethodNotSupportedException.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(errorDTO.getError()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorDTO>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("Handling MethodArgumentNotValidException: {} field errors", exception.getBindingResult().getErrorCount());
        List<ErrorDTO> errorDTOList = exception.getBindingResult().getAllErrors()
                .stream()
                .map(error -> buildErrorDTO(error.getDefaultMessage(), HttpStatus.BAD_REQUEST)
                )
                .toList();
        return ResponseEntity.status(errorDTOList.getFirst().getStatus()).body(errorDTOList);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleEnumException(HttpMessageNotReadableException exception) {
        log.warn("Handling HttpMessageNotReadableException: {}", exception.getMessage());
        String errorDetails = "Invalid request body format.";
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            if (targetType != null && targetType.isEnum()) {
                String fieldName = invalidFormatException.getPath().isEmpty() ? "unknown field" : invalidFormatException.getPath().getLast().getFieldName();

                log.warn("Invalid enum value for field {}: value={}, allowed={}", fieldName, invalidFormatException.getValue(), Arrays.toString(targetType.getEnumConstants()));

                errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        invalidFormatException.getValue(), fieldName,
                        Arrays.toString(targetType.getEnumConstants()));
            }
        }
        ErrorDTO errorDTO = buildErrorDTO(errorDetails, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error("Handling DataIntegrityViolationException: {}", ex.getMessage());
        String message = "Database constraint violation occurred.";

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null && rootCause.getMessage() != null) {
            String rawMessage = rootCause.getMessage();
            log.error("Root cause message: {}", rawMessage);

            if (rawMessage.contains("Duplicate entry")) {
                String duplicateValue = rawMessage.split("'")[1];
                message = String.format("'%s' is already exist. Please use a unique value.",
                        duplicateValue);
            }
        }

        ErrorDTO errorDTO = buildErrorDTO(message, HttpStatus.CONFLICT);
        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    private ErrorDTO buildErrorDTO(String message, HttpStatus httpStatus) {
        return ErrorDTO.builder()
                .message(message)
                .error(httpStatus)
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();
    }


}
