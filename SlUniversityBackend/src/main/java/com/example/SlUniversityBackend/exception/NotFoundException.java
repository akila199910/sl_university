package com.example.SlUniversityBackend.exception;

import lombok.Data;
import java.util.Map;

@Data
public class NotFoundException extends RuntimeException{
    private final Map<String, String> errors;
    private String message;
    private Boolean success;

    public NotFoundException(Map<String, String> errors,String message,Boolean success) {
        this.errors = errors;
        this.message = message;
        this.success = success;
    }
}
