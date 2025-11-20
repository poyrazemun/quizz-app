package com.poyraz.exceptions;

import java.io.Serial;

public class QuizNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public QuizNotFoundException(String message) {
        super(message);
    }
}
