package com.example.customer;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - بدنه‌ی درخواست بدفرمت یا JSON خراب
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> badBody(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request", req, null);
    }

    // 400 - ولیدیشن @Valid روی بدنه (DTO/Entity)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ValidationError> v = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ValidationError(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Validation failed", req, v);
    }

    // 400 - ولیدیشن روی پارامترهای URL/Query (@Min, @Email روی @RequestParam/@PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> constraint(ConstraintViolationException ex, HttpServletRequest req) {
        List<ValidationError> v = ex.getConstraintViolations().stream()
                .map(cv -> new ValidationError(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Constraint violation", req, v);
    }

    // 404 - پیدا نشد (وقتی خودت EntityNotFound پرتاب می‌کنی)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> notFound(EntityNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage() != null ? ex.getMessage() : "Resource not found", req, null);
    }

    // 409 - تعارض داده‌ای (کلید یکتا، Foreign key و ...)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> conflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Data integrity violation", req, null);
    }

    // 405 - متد پشتیبانی نمی‌شود
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> methodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed", req, null);
    }

    // 500 - هر چیز پیش‌بینی‌نشده
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unknown(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", req, null);
    }

    // ---- Helper ----
    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req, List<ValidationError> v) {
        ApiError body = new ApiError(status.value(), status.getReasonPhrase(), message, req.getRequestURI(), v);
        return ResponseEntity.status(status).body(body);
    }
}
