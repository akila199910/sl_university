package com.example.SlUniversityBackend.dto.User;

import com.example.SlUniversityBackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class UserResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private String contactNumber;
    private Boolean status;
    private ProfileDTO profile;
    private String editUrl;
    private String viewUrl;
    private String deleteUrl;
    private List<RoleDTO> roles;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileDTO {
        private Integer id;
        private String profileUrl;
        private String coverUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private Integer id;
        private String name;
    }
}
