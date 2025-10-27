package com.example.SlUniversityBackend.service.Admin;

import com.example.SlUniversityBackend.dto.Admin.UserCreateReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserProfileRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Page<UserResponseDTO> getUsers(Pageable pageable, String search) {

        Page<User> userPage; // Use Page<User>

        if (search != null && !search.trim().isEmpty()) {
            userPage = userRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        Page<UserResponseDTO> userResponseDTOPage = userPage.map(u -> {
            UserResponseDTO userDto = new UserResponseDTO();
            userDto.setId(u.getId());
            userDto.setFirstName(u.getFirstName());
            userDto.setLastName(u.getLastName());
            userDto.setName(u.getName());
            userDto.setEmail(u.getEmail());
            userDto.setContactNumber(u.getContactNumber());
            userDto.setStatus(u.getStatus());
            if (u.getRole() != null) {
                userDto.setRole(new UserResponseDTO.RoleDTO(u.getRole().getId(), u.getRole().getName()));
            } else {
                userDto.setRole(null);
            }
            if (u.getProfile() != null) {
                userDto.setProfile(new UserResponseDTO.ProfileDTO(u.getProfile().getId(), u.getProfile().getProfileUrl(), u.getProfile().getCoverUrl()));
            } else {
                userDto.setProfile(null);
            }
            return userDto;
        });

        return userResponseDTOPage;

    }
    public SuccessDTO createUser(UserCreateReqDTO userCreateReqDTO){
        Map<String, String> body = new HashMap<>();

        if(userRepository.existsByEmail(userCreateReqDTO.getEmail())){
            body.put("email", "Email is already taken.");
        }
        if(userRepository.existsByContactNumber(userCreateReqDTO.getContactNumber())){
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
        user.setFirstName(userCreateReqDTO.getFirstName());
        user.setLastName(userCreateReqDTO.getLastName());
        user.setName(userCreateReqDTO.getFirstName() + " " + userCreateReqDTO.getLastName());
        user.setEmail(userCreateReqDTO.getEmail());
        user.setContactNumber(userCreateReqDTO.getContactNumber());
        user.setStatus(userCreateReqDTO.getStatus());
        user.setPassword(userCreateReqDTO.getPassword());
        user.setRole(roleRepository.findById(Integer.parseInt(userCreateReqDTO.getRole())).get());
        user.setProfile(userProfile);
        userRepository.save(user);

        return new SuccessDTO("Admin created success.",true, null);
    }
}
