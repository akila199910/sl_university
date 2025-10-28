package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByContactNumber(String contactNumber);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByContactNumberAndIdNot(String contactNumber, Integer id);
    List<User> findAllByRoleId(Integer role);
    Optional<User> findByEmail(String email);
    Page<User> findByName(String name, Pageable pageable);

    Page<User> findByNameContainingIgnoreCase(String search, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseAndRoleId(String search, Role roleId, Pageable pageable);

    Page<User> findByRole(Role roleId, Pageable pageable);
}
