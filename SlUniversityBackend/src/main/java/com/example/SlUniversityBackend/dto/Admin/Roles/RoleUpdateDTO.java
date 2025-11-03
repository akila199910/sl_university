package com.example.SlUniversityBackend.dto.Admin.Roles;

import lombok.Data;

import java.util.Set;


@Data
public class RoleUpdateDTO {

    private Boolean status;

    private Set<Integer> permissions;
}
