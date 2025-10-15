package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
}
