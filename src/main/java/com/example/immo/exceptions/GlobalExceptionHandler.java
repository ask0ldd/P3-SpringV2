package com.example.immo.exceptions;

import com.example.immo.dto.responses.DefaultResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// handle validation & constraints exceptions
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new DefaultResponseDto(formatErrorMessage(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        return new ResponseEntity<>(new DefaultResponseDto(formatErrorMessage(ex.getMessage())), HttpStatus.BAD_REQUEST);
    }

    private String formatErrorMessage(String rawErrorMessage){
        return rawErrorMessage.substring(rawErrorMessage.lastIndexOf("[")+1).replace("]]", "").stripTrailing();
    }
}