package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    boolean existsByContactNumber(String contactNumber);
    List<User> findAllByRoleId(Integer role);
}
