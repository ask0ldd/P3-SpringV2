package com.example.immo.exceptions;

import java.io.Serial;

public class RentalNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 2;

    public RentalNotFoundException(String message) {
        super(message);
    }
}
