package com.example.SlUniversityBackend.controller.Admin;

import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/admins")
    public String test() {
        return "Access granted: USER_READ permission found";
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @GetMapping("/admin-only")
    public String adminTest() {
        return "Access granted: SUPER_ADMIN role found";
    }

    @GetMapping("/debug-auth")
    public ResponseEntity<?> debugAuth(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities());
    }


//    @Autowired
//    private UserService userService;
//
//    @GetMapping
//    public ResponseEntity<UserPageWithRolesDTO> getUsers(@RequestParam(required = false, defaultValue = "0") int page,
//                                                         @RequestParam(required = false, defaultValue = "10") int size,
//                                                         @RequestParam(required = false, defaultValue = "id") String sortBy,
//                                                         @RequestParam(required = false, defaultValue = "ASC") String sortDir,
//                                                         @RequestParam(required = false) String search,
//                                                         @RequestParam(required = false) String role ) {
//        Sort sort;
//        if(sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())){
//            sort = Sort.by(sortBy).ascending();
//        } else {
//            sort = Sort.by(sortBy).descending();
//        }
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        UserPageWithRolesDTO userPage = userService.getUsers(pageable, search, role);
//
//        return ResponseEntity.ok(userPage);
//
//    }
}
