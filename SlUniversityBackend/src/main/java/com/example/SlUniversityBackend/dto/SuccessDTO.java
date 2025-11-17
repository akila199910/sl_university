package com.example.SlUniversityBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SuccessDTO {
    private String message;
    private Boolean success = true;
    private Object data;
    private Boolean canAdd = false;

    public SuccessDTO(String message, Boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }




    public SuccessDTO(String message, Boolean success, Object data, Boolean canAdd) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.canAdd = canAdd;
    }




}
