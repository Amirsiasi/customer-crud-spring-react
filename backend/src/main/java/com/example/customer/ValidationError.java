package com.example.customer;

public class ValidationError {
    public String field;
    public String message;

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
