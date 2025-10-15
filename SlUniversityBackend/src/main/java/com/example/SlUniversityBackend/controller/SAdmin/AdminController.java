package com.example.SlUniversityBackend.controller.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.AdminReqDTO;
import com.example.SlUniversityBackend.dto.Admin.AdminResDTO;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.service.SAdmin.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminResDTO> createAdmin(@Valid @RequestBody AdminReqDTO adminReqDTO){
        return ResponseEntity.ok(adminService.createAdmin(adminReqDTO));
    }
}
