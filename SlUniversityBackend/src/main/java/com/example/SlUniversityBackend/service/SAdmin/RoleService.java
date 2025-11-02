package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.config.security.Roles;
import com.example.SlUniversityBackend.dto.Admin.Roles.RoleCreateDTO;
import com.example.SlUniversityBackend.dto.Admin.Roles.RoleResponseDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public SuccessDTO getAllRoles(Pageable pageable) {

        Page<Role> rolePage = roleRepository.findAll(pageable);

        Page<RoleResponseDTO> roleResponseDTOPage = rolePage.map(role -> {
            RoleResponseDTO dto = new RoleResponseDTO();
            dto.setName(role.getName());
            dto.setId(role.getId());

            List<RoleResponseDTO.PermissionList> permissionList = role.getPermissions()
                    .stream()
                    .map(permission -> new RoleResponseDTO.PermissionList(
                            permission.getId(),
                            permission.getName()
                    ))
                    .toList();

            dto.setPermissions(permissionList);
            return dto;
        });

        return new SuccessDTO(
                "Roles fetched successfully",
                true,
                roleResponseDTOPage
        );
    }

    public SuccessDTO roleCreate(RoleCreateDTO roleCreateDTO){
        Map<String, String> body = new HashMap<>();

        if(roleRepository.existsByName("ROLE_"+roleCreateDTO.getName())){
            body.put("name", "Already available this role");
            throw new DuplicateFieldException(body, "This name is already taken", false);
        }
        Role newRole = new Role();
        newRole.setName("ROLE_"+roleCreateDTO.getName());
        roleRepository.save(newRole);
        return new SuccessDTO("Role created success.", true, newRole.getName());
    }

}

