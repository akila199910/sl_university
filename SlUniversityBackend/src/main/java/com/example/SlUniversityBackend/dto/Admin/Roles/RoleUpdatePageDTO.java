package com.example.SlUniversityBackend.dto.Admin.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdatePageDTO {

    private List<PermissionListDTO> permissions;
    private String roleName;

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
        private boolean select;
    }
}
