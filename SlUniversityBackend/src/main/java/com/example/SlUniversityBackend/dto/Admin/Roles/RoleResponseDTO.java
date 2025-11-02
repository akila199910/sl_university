package com.example.SlUniversityBackend.dto.Admin.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class RoleResponseDTO {

    private Integer id;
    private String name;
    private List<PermissionList> permissions;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionList {
        private Integer id;
        private String name;
    }
}
