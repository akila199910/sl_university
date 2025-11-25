package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    boolean existsByContactNumber(String contactNumber);

    boolean existsByContactNumberAndIdNot(String email, Integer id);

    @EntityGraph(attributePaths = {"roles", "profile"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "profile"})
    Page<User> findByRoles(Role role, Pageable pageable);

    @EntityGraph(attributePaths = {"roles", "profile"})
    Page<User> findByNameContainingIgnoreCase(String search, Pageable pageable);

    @EntityGraph(attributePaths = {"roles", "profile"})
    Page<User> findByNameContainingIgnoreCaseAndRoles(String search, Role specificRole, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"roles", "profile"})
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"roles", "profile"})
    Optional<User> findById(Integer id);

}


