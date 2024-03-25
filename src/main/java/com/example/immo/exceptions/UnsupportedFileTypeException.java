package com.example.immo.exceptions;

import java.io.Serial;

public class UnsupportedFileTypeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4;

    public UnsupportedFileTypeException(String message) {
        super(message);
    }
}
