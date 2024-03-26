package com.example.immo.exceptions;

import java.io.Serial;

public class MessageNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3;

    public MessageNotFoundException(String message) {
        super(message);
    }
}
