package com.example.SlUniversityBackend.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class UserResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String contactNumber;
    private Boolean status;
    private RoleDTO role;
    private ProfileDTO profile;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private Integer id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileDTO {
        private String profileUrl;
        private String coverUrl;
    }
}
