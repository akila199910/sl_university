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
//    boolean existsByEmailAndIdNot(String email, Integer id);
//    boolean existsByContactNumberAndIdNot(String contactNumber, Integer id);

    Optional<User> findByEmail(String email);
    Page<User> findByNameContainingIgnoreCaseAndRolesNotContaining(String search, Role name, Pageable pageable);
    Page<User> findByNameContainingIgnoreCaseAndRolesNotContaining(String search, Optional<Role> role, Role name, Pageable pageable);
    Page<User> findByRolesNotContaining(Role byName,  Pageable pageable);
    Page<User> findByRoles(Optional<Role> byName, Pageable pageable);
}
