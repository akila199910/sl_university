package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.AdminReqDTO;
import com.example.SlUniversityBackend.dto.Admin.AdminResDTO;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserProfileRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static com.example.SlUniversityBackend.Enum.RoleName.admin;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    public AdminResDTO createAdmin(AdminReqDTO adminReqDTO){

        if(userRepository.existsByEmail(adminReqDTO.getEmail())){
           throw new DuplicateFieldException("email","Email is already taken.");
        }
        if(userRepository.existsByContactNumber(adminReqDTO.getContactNumber())){
            throw new DuplicateFieldException("contactNumber","Contact number is already taken.");
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
        user.setPassword(adminReqDTO.getPassword());
        user.setRole(roleRepository.findByName("admin"));
        user.setProfile(userProfile);
        userRepository.save(user);
       return ResponseEntity.ok(new AdminResDTO("Admin created success.",true)).getBody();
    }
}
