package com.example.SlUniversityBackend.config.exception;

import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.exception.RequestValidationFailException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<Object> handleDuplicate(DuplicateFieldException ex, HttpServletRequest request){
        Map<String, Object> body = new HashMap<>();
        body.put("success", ex.getSuccess());
        body.put("message", ex.getMessage());
        body.put("errors", ex.getErrors());
        body.put("path",request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        RequestValidationFailException validationFailException = new RequestValidationFailException(errors,"Validation fail",false);
        return handleRequestValidationFail(validationFailException,request);
    }

    @ExceptionHandler(RequestValidationFailException.class)
    public ResponseEntity<Object> handleRequestValidationFail(RequestValidationFailException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", ex.getSuccess());
        body.put("message", ex.getMessage());
        body.put("errors", ex.getErrors());
        body.put("path",request.getRequestURI());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
