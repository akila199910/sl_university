package com.example.SlUniversityBackend.config.seeder;

import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class RoleSeeder {

    @Value("${app.role}")
    private String[] roleNames;

    @Bean
    CommandLineRunner seedRoles(RoleRepository roles){
        return args -> createRole(roles);
    }

    @Transactional
    void createRole(RoleRepository roles){
        for (String roleName : roleNames){
            if(!roles.existsByName(roleName)){
                roles.save(new Role(roleName));
            }
        }
    }
}
