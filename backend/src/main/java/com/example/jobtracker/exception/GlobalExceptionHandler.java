package com.example.jobtracker.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * One place that turns exceptions into HTTP responses. Without this, an
 * exception escaping a controller becomes a generic 500 and the caller has no
 * idea what went wrong.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Thrown by Spring when @Valid fails. Every broken rule is joined into one
     * message so the user can fix them all at once instead of one per attempt.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message));
    }

    // 409 Conflict means "the request is valid, but it clashes with what's
    // already stored" - the right code for a duplicate.
    @ExceptionHandler(DuplicateUrlException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateUrlException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    /**
     * Safety net for the race between the service's existsByUrl check and the
     * insert: if two identical requests arrive at once, the database's UNIQUE
     * constraint rejects the second one. Catching it here keeps that a 409
     * instead of leaking a 500.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), "Already applied to this job"));
    }
}
