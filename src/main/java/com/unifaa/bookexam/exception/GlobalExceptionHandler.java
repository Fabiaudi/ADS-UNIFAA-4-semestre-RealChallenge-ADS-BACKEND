package com.unifaa.bookexam.exception;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.unifaa.bookexam.exception.error.BadRequestException;
import com.unifaa.bookexam.exception.error.ConflictException;
import com.unifaa.bookexam.exception.error.NotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail pd(HttpStatus status, String title, String detail) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail == null ? "" : detail);
        p.setTitle(title);
        p.setProperty("timestamp", OffsetDateTime.now());
        return p;
    }

    // === CUSTOM EXCEPTIONS ===
    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex) {
        return pd(HttpStatus.BAD_REQUEST, "Invalid request", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        return pd(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex) {
        return pd(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    // === FRAMEWORK VALIDATION ===
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
                .toList();
        ProblemDetail p = pd(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", "Request has invalid fields");
        p.setProperty("errors", errors);
        return p;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        return pd(HttpStatus.UNPROCESSABLE_ENTITY, "Constraint violation", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex) {
        return pd(HttpStatus.BAD_REQUEST, "Malformed JSON or invalid value", ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException ex) {
        return pd(HttpStatus.NOT_FOUND, "Entity not found", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        return pd(HttpStatus.CONFLICT, "Data integrity violation", ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage() : ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return pd(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex.getMessage());
    }
}
