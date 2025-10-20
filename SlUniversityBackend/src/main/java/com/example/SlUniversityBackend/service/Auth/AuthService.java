package com.example.SlUniversityBackend.service.Auth;

import com.example.SlUniversityBackend.JWT.JwtService;
import com.example.SlUniversityBackend.dto.Auth.LoginDTO;
import com.example.SlUniversityBackend.dto.Auth.LoginResDTO;
import com.example.SlUniversityBackend.dto.Auth.RegisterReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.entity.RefreshToken;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserProfileRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    public final HttpServletResponse response;
    private UserProfileRepository userProfileRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService, HttpServletResponse response,
                       UserProfileRepository userProfileRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.response = response;
        this.userProfileRepository = userProfileRepository;
    }

    public SuccessDTO register( RegisterReqDTO adminReqDTO) {

        Map<String, String> body = new HashMap<>();

        if(userRepository.existsByEmail(adminReqDTO.getEmail())){
            body.put("email", "Email is already taken.");
        }
        if(userRepository.existsByContactNumber(adminReqDTO.getContactNumber())){
            body.put("contactNumber", "Contact number is already taken.");
        }

        if (!body.isEmpty()){
            throw new DuplicateFieldException(body,"Duplicated unique values",false);
        }


        UserProfile userProfile = new UserProfile();
        userProfile.setProfileUrl("profile.png");
        userProfile.setCoverUrl("cover.png");
        userProfileRepository.save(userProfile);

        User user = new User();
        user.setFirstName(adminReqDTO.getFirstName());
        user.setLastName(adminReqDTO.getLastName());
        user.setName(adminReqDTO.getFirstName() + " " + adminReqDTO.getLastName());
        user.setEmail(adminReqDTO.getEmail());
        user.setContactNumber(adminReqDTO.getContactNumber());
        user.setStatus(adminReqDTO.getStatus());
        user.setPassword(passwordEncoder.encode(adminReqDTO.getPassword()));
        user.setRole(roleRepository.findByName("admin"));
        user.setProfile(userProfile);
        userRepository.save(user);

        return new SuccessDTO("Admin created success.",true, null);
    }


    public LoginResDTO login(LoginDTO authReq) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword())
        );

        String accessToken = jwtService.generateAccessToken(authReq.getEmail(), Map.of("roles", auth.getAuthorities()));

        // create refresh token and set httpOnly cookie
        User user = userRepository.findByEmail(authReq.getEmail()).orElseThrow();
        RefreshToken rt = refreshTokenService.createRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", rt.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/"); // cookie valid for your domain
        cookie.setMaxAge((int)( (double) (rt.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond()) ));
        // cookie.setSecure(true); // enable in prod with HTTPS


        response.addCookie(cookie);
        return new LoginResDTO(accessToken, "Bearer");
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
//        // read refresh token from cookie
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","No refresh token"));
//
//        String token = null;
//        for (Cookie c : cookies) {
//            if ("refreshToken".equals(c.getName())) {
//                token = c.getValue();
//            }
//        }
//        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","No refresh token"));
//
//        RefreshToken rt = refreshTokenService.findByToken(token);
//        if (rt == null || refreshTokenService.isTokenExpired(rt)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Refresh token invalid or expired"));
//        }
//
//        User user = rt.getUser();
//        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), Map.of("roles", user.getRole() != null ? user.getRole().getName() : "USER"));
//
//        // optionally rotate refresh token: delete old and issue new one
//        refreshTokenService.deleteByUser(user);
//        RefreshToken newRt = refreshTokenService.createRefreshToken(user);
//        Cookie cookie = new Cookie("refreshToken", newRt.getToken());
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge((int)((newRt.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond())));
//        // cookie.setSecure(true);
//        response.addCookie(cookie);
//
//        return ResponseEntity.ok(new LoginResDTO(newAccessToken, "Bearer"));
//    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        // delete refresh token cookie and DB entry
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie c : cookies) {
//                if ("refreshToken".equals(c.getName())) {
//                    String token = c.getValue();
//                    RefreshToken rt = refreshTokenService.findByToken(token);
//                    if (rt != null) refreshTokenService.deleteByUser(rt.getUser());
//                    // expire cookie
//                    Cookie cookie = new Cookie("refreshToken", null);
//                    cookie.setHttpOnly(true);
//                    cookie.setPath("/");
//                    cookie.setMaxAge(0);
//                    response.addCookie(cookie);
//                }
//            }
//        }
//        return ResponseEntity.ok(Map.of("message","Logged out"));
    }