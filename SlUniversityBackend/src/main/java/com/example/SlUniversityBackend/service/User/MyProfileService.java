package com.example.SlUniversityBackend.service.User;

import com.example.SlUniversityBackend.dto.Profile.MyProfileDTO;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.SlUniversityBackend.config.security.SecurityUtils.getCurrentUsername;

@Service
public class MyProfileService {
    @Autowired
    UserRepository userRepository;

    public MyProfileDTO getMyProfile() {

        String authUser = getCurrentUsername();
        Optional<User> user = userRepository.findByEmail(authUser);

        MyProfileDTO myProfileDTO = null;

        if (user.isPresent()) {
            User authUserData = user.get();

            myProfileDTO = new MyProfileDTO();

            List<MyProfileDTO.RoleDTO> roleDTOs = authUserData.getRoles().stream()
                    .map(role -> new MyProfileDTO.RoleDTO(role.getId(), role.getName()))
                    .toList();

            myProfileDTO.setId(authUserData.getId());
            myProfileDTO.setName(authUserData.getName());
            myProfileDTO.setEmail(authUserData.getEmail());
            myProfileDTO.setProfileImageUrl(authUserData.getProfile().getProfileUrl());
            myProfileDTO.setProfileImageUrl(authUserData.getProfile().getProfileUrl());
            myProfileDTO.setRoles(roleDTOs);

            return myProfileDTO;

        }

        return myProfileDTO;

    }
}
