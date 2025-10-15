package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.Enum.RoleName;
import com.example.SlUniversityBackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(String name);
}
