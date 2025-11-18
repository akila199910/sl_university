package com.example.SlUniversityBackend.dto.Admin.SystemUsers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.util.Set;

import static org.springframework.format.annotation.NumberFormat.Style.NUMBER;

@Data
public class SystemUserCreateDTO {

    @NotEmpty(message="First Name is required.")
    @Size(max = 15, message = "First name should be less than 15 characters.")
    private String firstName;

    @NotEmpty(message="Last Name is required.")
    @Size(max = 15, message = "Last name should be less than 15 characters.")
    private String lastName;

    @NotEmpty(message="Email is required.")
    @Email(message = "Email should be valid.")
    @Size(max = 30, message = "Email should be less than 30 characters.")
    private String email;

    @NotEmpty(message="Contact number is required.")
    @NumberFormat(style = NUMBER)
    @Size(max = 10, message = "Contact number should be less than 10 characters.")
    private String contactNumber;

    @NotEmpty(message="Select at least one role.")
    private Set<Integer> roles;
//    @NotEmpty(message = "Password is required.")
    @Size(max = 25, min = 8, message = "Password should be between 8 and 25 characters.")
    private String password;

    private Boolean status;

}
