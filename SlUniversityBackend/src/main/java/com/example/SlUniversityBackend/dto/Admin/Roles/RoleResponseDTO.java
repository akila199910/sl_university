package com.example.SlUniversityBackend.dto.Admin.Roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponseDTO {

    private Integer id;
    private String name;
    private Boolean status;
    private Set<PermissionList> permissions;
    private String viewUrl;
    private String editUrl;
    private String deleteUrl;
    private Boolean canCreate;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PermissionList {
        private Integer id;
        private String name;
    }
}
