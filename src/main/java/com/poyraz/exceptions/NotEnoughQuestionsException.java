package com.poyraz.exceptions;

import java.io.Serial;

public class NotEnoughQuestionsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public NotEnoughQuestionsException(String message) {
        super(message);
    }
}
