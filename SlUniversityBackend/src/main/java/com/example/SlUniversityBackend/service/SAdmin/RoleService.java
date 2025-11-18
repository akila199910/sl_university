package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.Roles.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.SlUniversityBackend.config.security.Permissions.*;
import static com.example.SlUniversityBackend.config.security.SecurityUtils.checkPermission;

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

        boolean canAdd = false;
        boolean[] checkList =  checkPermission(ROLE_READ, ROLE_CREATE, ROLE_UPDATE, ROLE_DELETE);
        if(checkList[1]){
            canAdd = true;
        }
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

            dto.setViewUrl(apiBaseUrl + "/roles/" + role.getId());
            dto.setEditUrl(apiBaseUrl + "/roles/" + role.getId());
            dto.setDeleteUrl(apiBaseUrl + "/roles/" + role.getId());

            return dto;
        });



        PagedModel<EntityModel<RoleResponseDTO>> pagedModel =
                pagedResourcesAssembler.toModel(roleResponseDTOPage);


        return new SuccessDTO(
                "Roles fetched successfully",
                true,
                pagedModel,
                canAdd
        );
    }

    public RoleCreatePageDTO createRole() {

        List<Permission> allPermissions = permissionRepository.findAll();

        Map<String, List<RoleCreatePageDTO.PermissionActionDTO>> groupedPermissions = allPermissions.stream()
                .filter(p -> p.getName().contains("_"))
                .collect(Collectors.groupingBy(

                        permission -> permission.getName().substring(0, permission.getName().indexOf("_")),


                        Collectors.mapping(
                                permission -> {
                                    String name = permission.getName();
                                    String action = name.substring(name.indexOf("_") + 1);
                                    return new RoleCreatePageDTO.PermissionActionDTO(permission.getId(), formatActionName(action));
                                },
                                Collectors.toList()
                        )
                ));

        List<RoleCreatePageDTO.PermissionListDTO> permissionListDTOS = groupedPermissions.entrySet().stream()
                .map(entry -> new RoleCreatePageDTO.PermissionListDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        RoleCreatePageDTO pageDTO = new RoleCreatePageDTO();
        pageDTO.setPermissions(permissionListDTOS);

        return pageDTO;
    }

    private String formatActionName(String action) {
        if (action == null || action.isEmpty()) {
            return "";
        }

        return Arrays.stream(action.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
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

        List<Permission> rolePermissions = findRole.getPermissions().stream().toList();
        List<Permission> allPermissions = permissionRepository.findAll();
        Set<Integer> rolePermissionIds = rolePermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        Map<String, List<RoleUpdatePageDTO.PermissionActionDTO>> groupedPermissions = allPermissions.stream()
                .filter(p -> p.getName().contains("_"))
                .collect(Collectors.groupingBy(
                        permission -> permission.getName().substring(0, permission.getName().indexOf("_")),

                        Collectors.mapping(
                                permission -> {
                                    String name = permission.getName();
                                    String action = name.substring(name.indexOf("_") + 1);

                                    boolean isChecked = rolePermissionIds.contains(permission.getId());

                                    return new RoleUpdatePageDTO.PermissionActionDTO(permission.getId(), formatActionName(action),isChecked);
                                },
                                Collectors.toList()
                        )
                ));

        List<RoleUpdatePageDTO.PermissionListDTO> permissionListDTOS = groupedPermissions.entrySet().stream()
                .map(entry -> new RoleUpdatePageDTO.PermissionListDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        RoleUpdatePageDTO pageDTO = new RoleUpdatePageDTO();
        pageDTO.setPermissions(permissionListDTOS);
        pageDTO.setRoleName(findRole.getName());
        pageDTO.setStatus(findRole.getStatus());


        return new SuccessDTO("Role fetch success", true, pageDTO);

    }

    private Set<RoleResponseDTO.PermissionList> mapPermissions(
            List<Permission> allPermissions,
            List<Permission> rolePermissions
    ) {

        Set<Integer> selectedIds = rolePermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        return allPermissions.stream()
                .map(p -> new RoleResponseDTO.PermissionList(
                        p.getId(),
                        p.getName(),
                        selectedIds.contains(p.getId()) // true if permission belongs to role
                ))
                .collect(Collectors.toSet());
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
//        if (roleUpdateDTO.getPermissions() == null){
//            roleUpdateDTO.setPermissions(findRole.getPermissions().stream().map(Permission::getId).collect(Collectors.toSet()));
//        }

        if (roleUpdateDTO.getPermissions() != null) {

            Set<Integer> previousPermissions = findRole.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toSet());

            List<Integer> newRequestPermission = roleUpdateDTO.getPermissions().stream().toList();

            Set<Integer> newRequestSet = new HashSet<>(newRequestPermission);

            List<Integer> newPermissions = newRequestPermission.stream()
                    .filter(permissionId -> !previousPermissions.contains(permissionId))
                    .toList();

            List<Integer> removePermissions = previousPermissions.stream()
                    .filter(pid -> !newRequestSet.contains(pid))
                    .toList();

            Set<Permission> currentPermissions = findRole.getPermissions();

            List<Permission> permissionsToRemove = currentPermissions.stream()
                    .filter(p -> removePermissions.contains(p.getId()))
                    .toList();

            permissionsToRemove.forEach(currentPermissions::remove);

            if (!newPermissions.isEmpty()) {
                List<Permission> permissionsToAdd = newPermissions.stream()
                        .map(permissionId -> permissionRepository.findById(permissionId)
                                .orElseThrow(() -> new NotFoundException(
                                        Map.of("permissionId", "No permission found with ID " + permissionId),
                                        "Permission not found.",
                                        false
                                )))
                        .toList();

                currentPermissions.addAll(permissionsToAdd);
            }

        }

        findRole.setStatus(roleUpdateDTO.getStatus());

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

