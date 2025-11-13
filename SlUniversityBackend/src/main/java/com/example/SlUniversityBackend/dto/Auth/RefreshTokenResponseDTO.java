package com.example.SlUniversityBackend.dto.Auth;

import com.example.SlUniversityBackend.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenResponseDTO {

    private String newAccessToken;
    private RefreshToken newRefreshToken;
}
