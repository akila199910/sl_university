package com.example.SlUniversityBackend.dto.Admin.Roles;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;
import java.util.Set;
import static org.springframework.format.annotation.NumberFormat.Style.NUMBER;

@Data
public class RoleCreateDTO {
    @NotEmpty(message="Name is required.")
    @Size(max = 15, message = "Name should be less than 15 characters.")
    private String name;

    @NotNull(message = "Status is required.")
    private Boolean status;


    @NumberFormat(style = NUMBER)
    private Set<Integer> permissions;
}
