package com.example.SlUniversityBackend.dto.Admin.SystemUsers;

import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserPageDTO {

    private PagedModel<EntityModel<UserResponseDTO>> userPage;
    private List<UserResponseDTO.RoleDTO> availableRoles;

}
