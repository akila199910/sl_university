package com.example.SlUniversityBackend.controller.Auth;

import com.example.SlUniversityBackend.dto.Auth.*;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.Auth.AuthService;
import com.example.SlUniversityBackend.service.Auth.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.access-token-expiration-ms}")
    long accessTokenValidityMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    long refreshTokenValidityMs;


    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<SuccessDTO> registerUser(@Valid @RequestBody RegisterReqDTO registerReqDTO){
        return ResponseEntity.ok(authService.register(registerReqDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessDTO> login(@Valid @RequestBody LoginDTO loginReqDTO, HttpServletResponse response){


        LoginResDTO loginResDTO = authService.login(loginReqDTO);

        Cookie accessTokenCookie =  new Cookie("access_token", loginResDTO.getAccessToken());
        accessTokenCookie.setMaxAge((int) (refreshTokenValidityMs/1000));
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refresh_token", loginResDTO.getRefreshToken());
        System.out.println(loginResDTO.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setMaxAge((int) (refreshTokenValidityMs/1000));
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(loginResDTO.getSuccessDTO());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh_token",required = false)String refreshToken, HttpServletResponse response){

        try {
            RefreshTokenResponseDTO refreshTokenResponseDTO = authService.refreshToken(refreshToken);
            Cookie accessTokenCookie =  new Cookie("access_token", refreshTokenResponseDTO.getNewAccessToken());
            accessTokenCookie.setMaxAge((int) (refreshTokenValidityMs/1000));
            accessTokenCookie.setSecure(false);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");

            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshTokenResponseDTO.getNewRefreshToken().getToken());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(false);
            refreshTokenCookie.setMaxAge((int) (refreshTokenValidityMs/1000));
            refreshTokenCookie.setPath("/");

            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok("Token refreshed successfully.");
        }catch (Exception e){
            clearCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session is expired. Please login again.");
        }

    }
    private void clearCookies(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
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
