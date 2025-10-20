package com.example.SlUniversityBackend.dto.Auth;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class LoginDTO {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
