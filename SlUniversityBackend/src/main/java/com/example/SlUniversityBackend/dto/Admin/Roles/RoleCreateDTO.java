package com.example.SlUniversityBackend.dto.Admin.Roles;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleCreateDTO {
    @NotEmpty(message="Name is required.")
    @Size(max = 15, message = "Name should be less than 15 characters.")
    private String name;
}
