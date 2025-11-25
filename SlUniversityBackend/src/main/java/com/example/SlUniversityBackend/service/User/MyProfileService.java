package com.example.SlUniversityBackend.service.User;

import com.example.SlUniversityBackend.dto.Profile.MyProfileDTO;
import com.example.SlUniversityBackend.dto.Profile.ProfileUpdateDTO;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
            myProfileDTO.setCoverImageUrl(authUserData.getProfile().getCoverUrl());
            myProfileDTO.setRoles(roleDTOs);
            myProfileDTO.setFirstName(authUserData.getFirstName());
            myProfileDTO.setLastName(authUserData.getLastName());
            myProfileDTO.setContactNumber(authUserData.getContactNumber());

            return myProfileDTO;

        }

        return myProfileDTO;

    }

    public ProfileUpdateDTO updateProfileData(ProfileUpdateDTO profileUpdateDTO){

        String authUser = getCurrentUsername();
        Optional<User> user = userRepository.findByEmail(authUser);

        if (user.isPresent()){
            User updateUser = user.get();

            HashMap<String,String> errors = new HashMap<>();
            if(userRepository.existsByContactNumberAndIdNot(profileUpdateDTO.getContactNumber(), updateUser.getId())){
                errors.put("contactNumber", "Contact number already taken.");
                throw new DuplicateFieldException(errors,"Validation fail.", false);
            }

            if(profileUpdateDTO.getContactNumber() == null){
                profileUpdateDTO.setContactNumber(updateUser.getContactNumber());
            }

            if(profileUpdateDTO.getFirstName() == null){
                profileUpdateDTO.setFirstName(updateUser.getFirstName());
            }

            if(profileUpdateDTO.getLastName() == null){
                profileUpdateDTO.setLastName(updateUser.getLastName());
            }

            updateUser.setContactNumber(profileUpdateDTO.getContactNumber());
            updateUser.setFirstName(profileUpdateDTO.getFirstName());
            updateUser.setLastName(profileUpdateDTO.getLastName());
            updateUser.setName(profileUpdateDTO.getFirstName()+" "+profileUpdateDTO.getLastName());

            userRepository.save(updateUser);

            return new ProfileUpdateDTO(updateUser.getFirstName(),updateUser.getLastName(),updateUser.getContactNumber());

        }

        return new ProfileUpdateDTO();
    }
}
