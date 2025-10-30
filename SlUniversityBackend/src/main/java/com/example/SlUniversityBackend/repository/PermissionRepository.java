package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface PermissionRepository extends JpaRepository<Permission , Integer> {
    boolean existsByName(String permName);
}
