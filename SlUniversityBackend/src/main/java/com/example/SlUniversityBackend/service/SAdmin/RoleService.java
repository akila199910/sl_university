package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.Roles.RoleCreateDTO;
import com.example.SlUniversityBackend.dto.Admin.Roles.RoleResponseDTO;
import com.example.SlUniversityBackend.dto.Admin.Roles.RoleUpdateDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.entity.Permission;
import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.exception.NotFoundException;
import com.example.SlUniversityBackend.repository.PermissionRepository;
import com.example.SlUniversityBackend.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    PagedResourcesAssembler<RoleResponseDTO> pagedResourcesAssembler;

    public SuccessDTO getAllRoles(Pageable pageable) {

        Page<Role> rolePage = roleRepository.findAll(pageable);

        Page<RoleResponseDTO> roleResponseDTOPage = rolePage.map(role -> {
            RoleResponseDTO dto = new RoleResponseDTO();
            dto.setId(role.getId());
            dto.setName(role.getName());
            dto.setStatus(role.getStatus());

            Set<RoleResponseDTO.PermissionList> permissionList = role.getPermissions()
                    .stream()
                    .map(permission -> new RoleResponseDTO.PermissionList(
                            permission.getId(),
                            permission.getName()
                    ))
                    .collect(Collectors.toSet());

            dto.setPermissions(permissionList);

            String encodedId = Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(role.getId()).getBytes());

            dto.setViewUrl(apiBaseUrl + "/roles/" + encodedId);
            dto.setEditUrl(apiBaseUrl + "/roles/" + encodedId);
            dto.setDeleteUrl(apiBaseUrl + "/roles/" + encodedId);

            return dto;
        });



        PagedModel<EntityModel<RoleResponseDTO>> pagedModel =
                pagedResourcesAssembler.toModel(roleResponseDTOPage);

        return new SuccessDTO(
                "Roles fetched successfully",
                true,
                pagedModel
        );
    }

    public SuccessDTO roleCreate(RoleCreateDTO roleCreateDTO) {
        Map<String, String> body = new HashMap<>();

        if (roleRepository.existsByName("ROLE_" + roleCreateDTO.getName())) {
            body.put("name", "Already available this role");
            throw new DuplicateFieldException(body, "This name is already taken", false);
        }

        Set<Permission> rolePermissionList = new HashSet<>();

        if (roleCreateDTO.getPermissions() != null && !roleCreateDTO.getPermissions().isEmpty()) {
            for (Integer permissionId : roleCreateDTO.getPermissions()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new NotFoundException(
                                Map.of("permissionId", "No permission found with ID " + permissionId),
                                "Permission not found.",
                                false
                        ));
                rolePermissionList.add(permission);
            }
        }

        Role newRole = new Role();
        newRole.setName("ROLE_" + roleCreateDTO.getName());
        newRole.setStatus(roleCreateDTO.getStatus());
        newRole.setPermissions(rolePermissionList);

        roleRepository.save(newRole);

        return new SuccessDTO("Role created successfully.", true, newRole.getName());
    }

    public SuccessDTO getRoleById(Integer id) {
        Role findRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        Map.of("roleId", "No role found with ID " + id),
                        "Role not found.",
                        false
                ));

        RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
        roleResponseDTO.setId(findRole.getId());
        roleResponseDTO.setName(findRole.getName());
        roleResponseDTO.setStatus(findRole.getStatus());
        roleResponseDTO.setPermissions(mapPermissions(findRole.getPermissions()));

        return new SuccessDTO("Role fetch success", true, roleResponseDTO);
    }

    private Set<RoleResponseDTO.PermissionList> mapPermissions(Set<Permission> list) {

        return list.stream()
                .map(p-> new RoleResponseDTO.PermissionList(p.getId(), p.getName())).collect(Collectors.toSet());
    }

    public SuccessDTO updateRole(@Valid @RequestBody RoleUpdateDTO roleUpdateDTO, Integer id){


        Role findRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        Map.of("roleId", "No role found with ID " + id),
                        "Role not found.",
                        false
                ));
        if (roleUpdateDTO.getStatus() == null){
            roleUpdateDTO.setStatus(findRole.getStatus());
        }
        if (roleUpdateDTO.getPermissions() == null){
            roleUpdateDTO.setPermissions(findRole.getPermissions().stream().map(Permission::getId).collect(Collectors.toSet()));
        }

        Set<Permission> permissionList = roleUpdateDTO.getPermissions().stream()
                .map(permissionId -> permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new NotFoundException(
                                Map.of("permissionId", "No permission found with ID " + permissionId),
                                "Permission not found.",
                                false
                        )))
                .collect(Collectors.toSet());

        findRole.setStatus(roleUpdateDTO.getStatus());
        findRole.setPermissions(permissionList);

        roleRepository.save(findRole);

        return new SuccessDTO(
                "Role updated successfully.",
                true,
                Map.of(
                        "id", findRole.getId(),
                        "name", findRole.getName(),
                        "permissions", findRole.getPermissions().stream()
                                .map(p -> new RoleResponseDTO.PermissionList(p.getId(), p.getName()))
                                .toList()
                )
        );

    }
}

