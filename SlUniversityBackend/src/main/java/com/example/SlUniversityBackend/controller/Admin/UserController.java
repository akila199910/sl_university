//package com.example.SlUniversityBackend.controller.Admin;
//
//import com.example.SlUniversityBackend.dto.Admin.users.UserPageWithRolesDTO;
//import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
//import com.example.SlUniversityBackend.service.Admin.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
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
//}
