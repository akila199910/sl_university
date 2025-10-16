package com.example.SlUniversityBackend.exception;

import java.util.Map;

public class DuplicateFieldException extends RuntimeException{

    private final Map<String, String> errors;
    private String message;
    private Boolean success;

    public DuplicateFieldException(Map<String, String> errors, String message, Boolean success) {
        this.errors = errors;
        this.message = message;
        this.success = success;
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

