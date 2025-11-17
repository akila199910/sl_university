package com.example.SlUniversityBackend.dto.Admin.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreatePageDTO {

    private List<PermissionListDTO> permissions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionListDTO {
        private String topic;
        private List<PermissionActionDTO> actions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionActionDTO {
        private Integer id;
        private String action;
    }
}