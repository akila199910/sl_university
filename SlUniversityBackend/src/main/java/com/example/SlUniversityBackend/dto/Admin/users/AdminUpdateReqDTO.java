package com.example.SlUniversityBackend.dto.Admin.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import static org.springframework.format.annotation.NumberFormat.Style.NUMBER;

@Data
public class AdminUpdateReqDTO {

    @Size(max = 15, message = "First name should be less than 15 characters.")
    private String firstName;

    @Size(max = 15, message = "Last name should be less than 15 characters.")
    private String lastName;

    @Email(message = "Email should be valid.")
    @Size(max = 30, message = "Email should be less than 30 characters.")
    private String email;

    @NumberFormat(style = NUMBER)
    @Size(max = 15, message = "Contact number should be less than 15 characters.")
    private String contactNumber;

    private Boolean status;

}
