package com.example.SlUniversityBackend.dto.Profile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDTO {

    @NotEmpty(message="First Name is required.")
    @Size(max = 15, message = "First name must be 15 characters or less.")
    private String firstName;

    @NotEmpty(message="Last Name is required.")
    // Last Name: Optional, but max 15 chars if present.
    @Size(max = 15, message = "Last name must be 15 characters or less.")
    private String lastName;

    @NotEmpty(message="Contact number is required.")
    // Contact Number: Optional, but if present, must be:
    // 1. Max 15 characters.
    // 2. Must consist only of digits (0-9).
    @Size(max = 15, message = "Contact number must be 15 characters or less.")
    @Pattern(regexp = "^[0-9]*$", message = "Contact number must contain only digits.")
    private String contactNumber;
}
