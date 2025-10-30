package com.example.SlUniversityBackend.service.User;

import com.example.SlUniversityBackend.entity.Permission;
import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email: " + email));

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Iterate through the user's roles (loaded eagerly due to FetchType.EAGER in User entity)
        for (Role role : u.getRoles()) {
            // Add the role itself as an authority (with ROLE_ prefix)
            authorities.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));

            // Iterate through the permissions of that role (loaded eagerly due to FetchType.EAGER in Role entity)
            for (Permission permission : role.getPermissions()) {
                // Add each permission as an authority
                authorities.add(new SimpleGrantedAuthority(permission.getName().toUpperCase()));
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(authorities)
                .accountExpired(false).accountLocked(false).credentialsExpired(false)
                .disabled(false)
//                .disabled(!(u.getStatus() != null && u.getStatus()))
                .build();
    }
}