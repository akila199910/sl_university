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

    @Data
    public static class PermissionList {

        private Integer id;
        private String name;
        private boolean select;

        public PermissionList(Integer id, String name, boolean select) {
            this.id = id;
            this.name = name;
            this.select = select;
        }

        public PermissionList(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

    }
}
