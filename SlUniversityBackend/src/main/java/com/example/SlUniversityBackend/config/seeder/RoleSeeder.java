package com.example.SlUniversityBackend.config.seeder;

import com.example.SlUniversityBackend.config.security.Permissions;
import com.example.SlUniversityBackend.config.security.Roles;
import com.example.SlUniversityBackend.entity.Permission;
import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.repository.PermissionRepository;
import com.example.SlUniversityBackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class RoleSeeder {

    private static final List<String> ALL_Roles = Arrays.asList(
            Roles.ROLE_SUPER_ADMIN
    );

    @Bean
    CommandLineRunner seedRoles(RoleRepository roles, PermissionRepository permissionRepository){
        return args -> createRole(roles,permissionRepository);
    }

    @Transactional
    void createRole(RoleRepository roles, PermissionRepository permissionRepository){

        // 1. Fetch all existing permissions once
        List<Permission> allPermissions = permissionRepository.findAll();

        for (String roleName : ALL_Roles){

            Role role;

            // 2. Optimized Role Retrieval/Creation
            if(!roles.existsByName(roleName)){
                // Create and capture the saved instance to avoid another findByName()
                role = roles.save(new Role(roleName, true));
            } else {
                role = roles.findByName(roleName);
            }

            // 3. Create a Set of existing permission IDs for fast lookup
            Set<Integer> existingPermissionIds = role.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(java.util.stream.Collectors.toSet());

            // 4. Check if we need to update the permissions
            if (existingPermissionIds.size() != allPermissions.size()) {

                Set<Permission> permissionsToSave = new HashSet<>(role.getPermissions());
                boolean permissionsUpdated = false;

                // 5. Iterate through all permissions and add any missing ones
                for (Permission permission : allPermissions) {
                    if (!existingPermissionIds.contains(permission.getId())) {
                        permissionsToSave.add(permission);
                        permissionsUpdated = true;
                    }
                }

                // 6. Only save if an update was actually needed
                if (permissionsUpdated) {
                    role.setPermissions(permissionsToSave);
                    roles.save(role); // Explicitly save the updated role within the transaction
                }
            }
        }
    }
}
