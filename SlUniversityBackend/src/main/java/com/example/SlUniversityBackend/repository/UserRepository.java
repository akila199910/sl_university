package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByContactNumber(String contactNumber);


    Optional<User> findByEmail(String email);

    Page<User> findByRoles(Optional<Role> byName, Pageable pageable);

    Page<User> findByNameContainingIgnoreCase(String search, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseAndRoles(String search, Role specificRole, Pageable pageable);
}
