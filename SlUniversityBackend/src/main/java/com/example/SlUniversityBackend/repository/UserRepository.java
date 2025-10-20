package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.User;
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
}
