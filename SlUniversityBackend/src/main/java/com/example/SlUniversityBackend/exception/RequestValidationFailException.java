package com.example.SlUniversityBackend.exception;

import java.util.Map;

public class RequestValidationFailException extends RuntimeException {

    private final Map<String, String> errors;
    private String message;
    private Boolean success;

    public RequestValidationFailException(Map<String, String> errors, String message, Boolean success) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
    public String getMessage() {
        return message;
    }

    public Boolean getSuccess(){
        return success;
    }
}
