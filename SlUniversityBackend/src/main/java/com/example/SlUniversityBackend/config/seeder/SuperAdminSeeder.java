package com.example.SlUniversityBackend.config.seeder;

import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class SuperAdminSeeder {

    @Bean
    CommandLineRunner seedSuperAdmin(UserRepository users,
                                     RoleRepository roles,
                                     PasswordEncoder encoder) {
        return args -> createSuperAdmin(users, roles, encoder);
    }

    @Transactional
    void createSuperAdmin(UserRepository users,
                          RoleRepository roles,
                          PasswordEncoder encoder) {

        // 1) Skip if already present
        Optional<User> existing = users.findByEmail("akilaumayangaw@gmail.com");
        if (existing.isPresent()) {
            System.out.println("✅ Super Admin exists. Skipping.");
            return;
        }

        // 2) Ensure role exists
        Role superAdmin = roles.findByName("ROLE_SUPER_ADMIN");
        if (superAdmin == null) {
            superAdmin = roles.save(new Role("ROLE_SUPER_ADMIN"));
        }

        // 3) Build UserProfile but DON'T save separately (cascades from User)
        UserProfile profile = new UserProfile();
        profile.setProfileUrl("profile.png");
        profile.setCoverUrl("cover.png");

        // 4) Build User and attach profile + roles
        User user = new User();
        user.setFirstName("Akila");
        user.setLastName("Umayanga");
        user.setName("Akila Umayanga");
        user.setEmail("akilaumayangaw@gmail.com");
        user.setContactNumber("0702024999"); // string if your field is String
        user.setStatus(true);
        user.setPassword(encoder.encode("Admin@1234"));

        Set<Role> rs = new HashSet<>();
        rs.add(superAdmin);
        user.setRoles(rs);

        user.setProfile(profile);    // profile cascades

        // 5) Save once
        users.save(user);

        System.out.println("✅ Super Admin seeded.");
    }
}