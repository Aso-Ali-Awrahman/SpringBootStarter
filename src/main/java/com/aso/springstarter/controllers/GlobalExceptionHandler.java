package com.aso.springstarter.controllers;

import java.time.Instant;
import java.util.HashMap;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleNotFoundException(ResponseStatusException ex) {
        final var error = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        error.setProperty("timestamp", Instant.now());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        final var error = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), "Validation failed for one or more fields");
        error.setProperty("timestamp", Instant.now());
        final var validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
            validationErrors.put(err.getField(), err.getDefaultMessage())
        );
        error.setProperty("errors", validationErrors);
        return error;
    }

}
