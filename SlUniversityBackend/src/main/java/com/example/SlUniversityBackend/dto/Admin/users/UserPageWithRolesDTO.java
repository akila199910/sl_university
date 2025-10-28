package com.example.SlUniversityBackend.dto.Admin.users;

import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import com.example.SlUniversityBackend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageWithRolesDTO {
    private Page<UserResponseDTO> userPage;
    private List<Role> availableRoles;
}
