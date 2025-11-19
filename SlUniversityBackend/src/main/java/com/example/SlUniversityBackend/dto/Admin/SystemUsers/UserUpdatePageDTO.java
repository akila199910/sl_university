package com.example.SlUniversityBackend.dto.Admin.SystemUsers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatePageDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private Boolean status;
    private String profile;
    private List<RoleDTO> roles;
    private boolean canUpdate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private Integer id;
        private String name;
        private boolean select;
    }

}
