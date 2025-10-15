package com.example.SlUniversityBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 15)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 15)
    private String lastName;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "email", nullable = false, length = 30, unique = true)
    private String email;

    @Column(name = "contact_number", nullable = false, length = 15, unique = true)
    private String contactNumber;

    @Column(name = "password", nullable = false, length = 25)
    private String password;

    @Column(name = "status", nullable = true)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = true)
    private UserProfile profile;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
