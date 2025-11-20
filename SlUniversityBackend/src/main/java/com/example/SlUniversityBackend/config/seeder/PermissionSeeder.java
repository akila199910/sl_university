package com.example.SlUniversityBackend.config.seeder;

import com.example.SlUniversityBackend.config.security.Permissions;
import com.example.SlUniversityBackend.entity.Permission;
import com.example.SlUniversityBackend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class PermissionSeeder implements CommandLineRunner {

    @Autowired
    private PermissionRepository permissionRepository;

    private static final List<String> ALL_PERMISSIONS = Arrays.asList(

            Permissions.ROLE_READ,
            Permissions.ROLE_CREATE,
            Permissions.ROLE_UPDATE,
            Permissions.ROLE_DELETE,

            Permissions.USER_READ,
            Permissions.USER_CREATE,
            Permissions.USER_UPDATE,
            Permissions.USER_DELETE
    );


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Seeding Permissions...");
        for (String permName : ALL_PERMISSIONS) {
            if (!permissionRepository.existsByName(permName)) {
                permissionRepository.save(new Permission(permName));
                System.out.println("Created Permission: " + permName);
            }
        }
        System.out.println("Permission seeding complete.");
    }
}
