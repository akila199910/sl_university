package com.example.SlUniversityBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessDTO {
    private String message;
    private Boolean success = true;
    private Object data;
}
