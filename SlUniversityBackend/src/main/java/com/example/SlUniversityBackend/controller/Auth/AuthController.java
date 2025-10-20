package com.example.SlUniversityBackend.controller.Auth;

import com.example.SlUniversityBackend.dto.Auth.LoginDTO;
import com.example.SlUniversityBackend.dto.Auth.LoginResDTO;
import com.example.SlUniversityBackend.dto.Auth.RegisterReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.Auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessDTO> registerUser(@Valid @RequestBody RegisterReqDTO registerReqDTO){
        return ResponseEntity.ok(authService.register(registerReqDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@Valid @RequestBody LoginDTO loginReqDTO){
        System.out.println("loginReqDTO");
        return ResponseEntity.ok(authService.login(loginReqDTO));
    }
}
