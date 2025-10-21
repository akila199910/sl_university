package com.example.SlUniversityBackend.repository;

import com.example.SlUniversityBackend.entity.RefreshToken;
import com.example.SlUniversityBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
}
