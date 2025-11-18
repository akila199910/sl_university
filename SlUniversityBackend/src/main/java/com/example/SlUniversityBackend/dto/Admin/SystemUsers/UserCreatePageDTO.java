package com.example.SlUniversityBackend.dto.Admin.SystemUsers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatePageDTO {

    private List<RoleItem> roles;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleItem {
        private Integer id;
        private String name;
    }
}
