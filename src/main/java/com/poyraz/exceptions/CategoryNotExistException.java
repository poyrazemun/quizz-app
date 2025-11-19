package com.poyraz.exceptions;

import java.io.Serial;

public class CategoryNotExistException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CategoryNotExistException(String message) {
        super(message);
    }
}
