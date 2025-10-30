package com.example.SlUniversityBackend.config.seeder;

import com.example.SlUniversityBackend.config.security.Permissions;
import com.example.SlUniversityBackend.entity.Permission;
import com.example.SlUniversityBackend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class PermissionSeeder implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    // Add your new permission constants here
    private static final List<String> ALL_PERMISSIONS = Arrays.asList(

            Permissions.ADMIN_READ,
            Permissions.ADMIN_CREATE,
            Permissions.ADMIN_UPDATE,
            Permissions.ADMIN_DELETE,
            Permissions.USER_READ,
            Permissions.USER_CREATE,
            Permissions.USER_UPDATE,
            Permissions.USER_DELETE,
            Permissions.ROLE_READ,     // Added
            Permissions.ROLE_CREATE,   // Added
            Permissions.ROLE_UPDATE,   // Added
            Permissions.ROLE_DELETE,   // Added
            Permissions.PERMISSION_READ, // Added
            Permissions.PERMISSION_CREATE, // Added
            Permissions.PERMISSION_UPDATE, // Added
            Permissions.PERMISSION_DELETE  // Added
            // ... other permissions ...
    );


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Seeding Permissions...");
        for (String permName : ALL_PERMISSIONS) {
            if (!permissionRepository.existsByName(permName)) { // Checks if exists
                permissionRepository.save(new Permission(permName)); // Saves if not exists
                System.out.println("Created Permission: " + permName);
            }
        }
        System.out.println("Permission seeding complete.");
    }
}
