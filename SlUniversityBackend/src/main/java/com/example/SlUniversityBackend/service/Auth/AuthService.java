package com.example.SlUniversityBackend.service.Auth;

import com.example.SlUniversityBackend.JWT.JwtService;
import com.example.SlUniversityBackend.config.security.Roles;
import com.example.SlUniversityBackend.dto.Auth.*;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.entity.RefreshToken;
import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.exception.NotFoundException;
import com.example.SlUniversityBackend.exception.RequestValidationFailException;
import com.example.SlUniversityBackend.repository.RefreshTokenRepository;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserProfileRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import com.example.SlUniversityBackend.service.User.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final HttpServletResponse response;
    private final UserProfileRepository userProfileRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, HttpServletResponse response,
                       UserProfileRepository userProfileRepository, CustomUserDetailsService customUserDetailsService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.response = response;
        this.userProfileRepository = userProfileRepository;
        this.customUserDetailsService = customUserDetailsService;
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
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(Roles.ROLE_ADMIN));
        user.setRoles(roles);
        user.setProfile(userProfile);
        userRepository.save(user);

        return new SuccessDTO("User register success.",true, null);
    }


    public LoginResDTO login(LoginDTO authReq) {

        User  searchUser = userRepository.findByEmail(authReq.getEmail()).orElseThrow(
                ()-> new NotFoundException(Map.of(
                        "email", "Email is not found."),
                        "Email not found.",
                        false)
        );


        if (!passwordEncoder.matches(authReq.getPassword(), searchUser.getPassword())) {
            throw new RequestValidationFailException(
                    Map.of("password", "Password is incorrect."),
                    "Invalid login credentials.",
                    false
            );
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword())
        );

        String accessToken = jwtService.generateAccessToken(authReq.getEmail(), Map.of("roles", auth.getAuthorities()));

        User user = userRepository.findByEmail(authReq.getEmail()).orElseThrow(
                ()-> new NotFoundException(Map.of(
                        "email", "No user found "),
                        "User not found.",
                        false)
        );
        RefreshToken rt = refreshTokenService.createRefreshToken(user);

        Map<String, Object>  loginData = new HashMap<>();

        List<RoleDTO> roleDTOs = user.getRoles().stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .toList();

        loginData.put("id", user.getId());
        loginData.put("name", user.getName());
        loginData.put("email", user.getEmail());
        loginData.put("roles",roleDTOs);
        loginData.put("profileImageUrl", user.getProfile().getProfileUrl());
        loginData.put("coverImageUrl", user.getProfile().getCoverUrl());
        loginData.put("refreshToken", rt.getToken());
        loginData.put("accessToken", accessToken);


        SuccessDTO successDTO = new SuccessDTO("Login Success", true,loginData);

         return new LoginResDTO(accessToken,rt.getToken(),successDTO);
    }

    public RefreshTokenResponseDTO refreshToken(String token) {

        if (token == null) {
            throw new RequestValidationFailException(
                    Map.of("refreshToken", "Refresh token is required."),
                    "Authentication fail.",
                    false
            );
        }

        RefreshToken rt = refreshTokenService.findByToken(token);

        if (rt == null || refreshTokenService.isTokenExpired(rt)) {
            throw new RequestValidationFailException(
                    Map.of("refreshToken", "Refresh token is invalid or expired"),
                    "Authentication fail.",
                    false
            );
        }

        User user = rt.getUser();

        RefreshToken newRt = refreshTokenService.createRefreshToken(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(), Map.of("roles", userDetails.getAuthorities()));

        return new RefreshTokenResponseDTO(newAccessToken, newRt);
    }

    public SuccessDTO logout(HttpServletRequest request, HttpServletResponse response){
        // delete refresh token cookie and DB entry
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("refreshToken".equals(c.getName())) {
                    String token = c.getValue();
                    RefreshToken rt = refreshTokenService.findByToken(token);
                    if (rt != null) refreshTokenService.deleteByUser(rt.getUser());
                    // expire cookie
                    Cookie cookie = new Cookie("refreshToken", null);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return new SuccessDTO("Log out user.",true,null);
    }

    public SuccessDTO me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> authUser = null;
        if (authentication != null) {
            String username = authentication.getName();

            User user = userRepository.findByEmail(username).orElseThrow(
                    () -> new NotFoundException(Map.of(
                            "email", "No user found "),
                            "User not found.",
                            false)
            );

            authUser = new HashMap<>();
            authUser.put("id", user.getId());
            authUser.put("email", username);
            authUser.put("role", user.getRoles());

        }

        return new SuccessDTO("User register success.", true, authUser);
    }
}