package com.example.SlUniversityBackend.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResDTO {
    private String accessToken;
    private String tokenType = "Bearer";
}
