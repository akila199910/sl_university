//package com.example.SlUniversityBackend.service.Admin;
//
//import com.example.SlUniversityBackend.dto.Admin.users.UserCreateReqDTO;
//import com.example.SlUniversityBackend.dto.Admin.users.UserPageWithRolesDTO;
//import com.example.SlUniversityBackend.dto.SuccessDTO;
//import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
//import com.example.SlUniversityBackend.entity.Role;
//import com.example.SlUniversityBackend.entity.User;
//import com.example.SlUniversityBackend.entity.UserProfile;
//import com.example.SlUniversityBackend.exception.DuplicateFieldException;
//import com.example.SlUniversityBackend.exception.NotFoundException;
//import com.example.SlUniversityBackend.repository.RoleRepository;
//import com.example.SlUniversityBackend.repository.UserProfileRepository;
//import com.example.SlUniversityBackend.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Service
//public class UserService {
//
//    @Value("${app.api.base-url}")
//    private String apiBaseUrl;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    public UserPageWithRolesDTO getUsers(Pageable pageable, String search, String roleId)  {
//
//        Page<User> userPage;
//        boolean hasSearch = search != null && !search.trim().isEmpty();
//        boolean hasRoleId = roleId != null && !roleId.trim().isEmpty();
//        Map<String,String> errors = new HashMap<>();
//        Role role = null;
//        if(hasRoleId){
//             role = roleRepository.findById(Integer.parseInt(roleId)).orElseThrow(()-> new NotFoundException(Map.of(
//                    "roleId", "No role found with ID " + roleId),
//                    "Role not found.",
//                    false)
//            );
//        }
//
//
//        if (hasSearch && hasRoleId) {
//            userPage = userRepository.findByNameContainingIgnoreCaseAndRoleId(search, role, pageable);
//        } else if (hasSearch) {
//            userPage = userRepository.findByNameContainingIgnoreCase(search, pageable);
//        } else if (hasRoleId) {
//            userPage = userRepository.findByRole(role, pageable);
//        } else {
//            // NO filter (fetch all, paginated)
//            userPage = userRepository.findAll(pageable);
//        }
//
//         get the available roles from role table
//        List<Role> availableRoles = roleRepository.findAll();
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        boolean canSeeUrls = false;
//        if (authentication != null && authentication.isAuthenticated()) {
//            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//            for (GrantedAuthority authority : authorities) {
//                String authUserRoles = authority.getAuthority();
//
//                if ("SUPER ADMIN".equals(authUserRoles) || "ADMIN".equals(authUserRoles)) {
//                    canSeeUrls = true;
//                    break;
//                }
//            }
//        }
//
//        boolean finalCanSeeUrls = canSeeUrls;
//        Page<UserResponseDTO> userResponseDTOPage = userPage.map(u -> {
//            UserResponseDTO userDto = new UserResponseDTO();
//            userDto.setId(u.getId());
//            userDto.setFirstName(u.getFirstName());
//            userDto.setLastName(u.getLastName());
//            userDto.setName(u.getName());
//            userDto.setEmail(u.getEmail());
//            userDto.setContactNumber(u.getContactNumber());
//            userDto.setStatus(u.getStatus());
//            if (u.getRoles() != null) {
////                userDto.setRole(new UserResponseDTO.RoleDTO(u.getRoles().);
//            } else {
//                userDto.setRole(null);
//            }
//            if (u.getProfile() != null) {
//                userDto.setProfile(new UserResponseDTO.ProfileDTO(u.getProfile().getId(), u.getProfile().getProfileUrl(), u.getProfile().getCoverUrl()));
//            } else {
//                userDto.setProfile(null);
//            }
//
//            if(finalCanSeeUrls){
//                String encodedId = Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(u.getId()).getBytes());
//                userDto.setViewUrl(apiBaseUrl + "/users/" + encodedId);
//                userDto.setEditUrl(apiBaseUrl + "/users/" + encodedId);
//                userDto.setDeleteUrl(apiBaseUrl + "/users/" + encodedId);
//            }
//
//            return userDto;
//        });
//
//
//        return new UserPageWithRolesDTO(userResponseDTOPage,availableRoles);
//
//    }
//    public SuccessDTO createUser(UserCreateReqDTO userCreateReqDTO){
//        Map<String, String> body = new HashMap<>();
//
//        if(userRepository.existsByEmail(userCreateReqDTO.getEmail())){
//            body.put("email", "Email is already taken.");
//        }
//        if(userRepository.existsByContactNumber(userCreateReqDTO.getContactNumber())){
//            body.put("contactNumber", "Contact number is already taken.");
//        }
//
//        if (!body.isEmpty()){
//            throw new DuplicateFieldException(body,"Duplicated unique values",false);
//        }
//
//        UserProfile userProfile = new UserProfile();
//        userProfile.setProfileUrl("profile.png");
//        userProfile.setCoverUrl("cover.png");
//        userProfileRepository.save(userProfile);
//
//        User user = new User();
//        user.setFirstName(userCreateReqDTO.getFirstName());
//        user.setLastName(userCreateReqDTO.getLastName());
//        user.setName(userCreateReqDTO.getFirstName() + " " + userCreateReqDTO.getLastName());
//        user.setEmail(userCreateReqDTO.getEmail());
//        user.setContactNumber(userCreateReqDTO.getContactNumber());
//        user.setStatus(userCreateReqDTO.getStatus());
//        user.setPassword(userCreateReqDTO.getPassword());
//        user.setRoles();
//        user.setProfile(userProfile);
//        userRepository.save(user);
//
//        return new SuccessDTO("Admin created success.",true, null);
//    }
//}
