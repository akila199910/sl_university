package com.example.SlUniversityBackend.dto.Auth;

import com.example.SlUniversityBackend.dto.SuccessDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResDTO {
    private String accessToken;
    private String refreshToken;
    private SuccessDTO successDTO;
}
