package com.example.SlUniversityBackend.controller.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.AdminReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import com.example.SlUniversityBackend.service.SAdmin.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }

    @PostMapping
    public ResponseEntity<SuccessDTO> createAdmin(@Valid @RequestBody AdminReqDTO adminReqDTO){
        return ResponseEntity.ok(adminService.createAdmin(adminReqDTO));
    }
}
