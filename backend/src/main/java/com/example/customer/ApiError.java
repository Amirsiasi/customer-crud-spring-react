package com.example.customer;

import java.util.List;

public class ApiError {

    public int status;
    public String error;
    public String message;
    public String path;
    public List<ValidationError> validationErrors;

    public ApiError(int status, String error, String message, String path,List<ValidationError> validationErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = validationErrors;
    }
}
