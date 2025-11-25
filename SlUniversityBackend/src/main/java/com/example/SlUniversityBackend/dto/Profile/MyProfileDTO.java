package com.example.SlUniversityBackend.dto.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileDTO {
    private Integer id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private List<RoleDTO> roles;
    private String profileImageUrl;
    private String coverImageUrl;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private Integer id;
        private String name;
    }

}
