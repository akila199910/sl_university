package com.example.SlUniversityBackend.controller.Auth;

import com.example.SlUniversityBackend.dto.Auth.LoginDTO;
import com.example.SlUniversityBackend.dto.Auth.RegisterReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.Auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessDTO> login(@Valid @RequestBody LoginDTO loginReqDTO){
        return ResponseEntity.ok(authService.login(loginReqDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessDTO> refreshToken(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.refreshToken(request,response));
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(request,response));
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessDTO> me() {
        return ResponseEntity.ok(authService.me());
    }

}
