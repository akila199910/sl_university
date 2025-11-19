package com.example.SlUniversityBackend.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }


    public static Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities();
        }
        return null;
    }

    public static boolean hasRole(String roleName) {
        Collection<? extends GrantedAuthority> authorities = getCurrentUserAuthorities();
        if (authorities == null) return false;
        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase(roleName));
    }


    public static boolean isSuperAdmin() {
        return hasRole("ROLE_SUPER_ADMIN");
    }


    public static Object getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getPrincipal() : null;
    }

    public static boolean[] checkPermission(String read, String create, String update, String delete) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean[] result = {false, false, false, false};

        if (authentication == null || authentication.getAuthorities() == null) {
            return result;
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().toUpperCase())
                .collect(Collectors.toSet());

        if (read != null && authorities.contains(read.toUpperCase())) result[0] = true;
        if (create != null && authorities.contains(create.toUpperCase())) result[1] = true;
        if (update != null && authorities.contains(update.toUpperCase())) result[2] = true;
        if (delete != null && authorities.contains(delete.toUpperCase())) result[3] = true;

        return result;
    }

    public static boolean checkPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean result = false;

        if (authentication == null || authentication.getAuthorities() == null) {
            return result;
        }

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().toUpperCase())
                .collect(Collectors.toSet());

        if(authorities.contains(permission.toUpperCase())){
            result = true;
        }

        return result;
    }

}
