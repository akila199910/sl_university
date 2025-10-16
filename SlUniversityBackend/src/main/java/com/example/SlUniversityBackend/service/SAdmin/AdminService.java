package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.AdminReqDTO;
import com.example.SlUniversityBackend.dto.Admin.AdminUpdateReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.exception.NotFoundException;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserProfileRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<UserResponseDTO> getUsers(){
        List<User> users = userRepository.findAllByRoleId(2);

        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();


        users.forEach(u ->{
            UserResponseDTO userDto = new  UserResponseDTO();
            userDto.setId(u.getId());
            userDto.setFirstName(u.getFirstName());
            userDto.setLastName(u.getLastName());
            userDto.setName(u.getName());
            userDto.setEmail(u.getEmail());
            userDto.setContactNumber(u.getContactNumber());
            userDto.setStatus(u.getStatus());
            userDto.setRole(new UserResponseDTO.RoleDTO(u.getRole().getId(), u.getRole().getName()));
            userDto.setProfile(new UserResponseDTO.ProfileDTO(u.getProfile().getId(),u.getProfile().getProfileUrl(),u.getProfile().getCoverUrl()));
            userResponseDTOList.add(userDto);
        });

       return userResponseDTOList;
    }


    public SuccessDTO createAdmin(AdminReqDTO adminReqDTO){

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
        user.setPassword(adminReqDTO.getPassword());
        user.setRole(roleRepository.findByName("admin"));
        user.setProfile(userProfile);
        userRepository.save(user);
       return new SuccessDTO("Admin created success.",true);
    }

    public UserResponseDTO getUserById(Integer id){

        User u = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Map.of(
                        "userId", "No user found with ID " + id),
                        "User not found.",
                        false)
                );

            UserResponseDTO userDto = new  UserResponseDTO();
            userDto.setId(u.getId());
            userDto.setFirstName(u.getFirstName());
            userDto.setLastName(u.getLastName());
            userDto.setName(u.getName());
            userDto.setEmail(u.getEmail());
            userDto.setContactNumber(u.getContactNumber());
            userDto.setStatus(u.getStatus());
            userDto.setRole(new UserResponseDTO.RoleDTO(u.getRole().getId(), u.getRole().getName()));
            userDto.setProfile(new UserResponseDTO.ProfileDTO(u.getProfile().getId(),u.getProfile().getProfileUrl(),u.getProfile().getCoverUrl()));
            return userDto;
    }

    public SuccessDTO updateAdmin(Integer id, AdminUpdateReqDTO adminUpdateReqDTO){
        User u = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Map.of(
                        "userId", "No user found with ID " + id),
                        "User not found.",
                        false)
                );

        Map<String, String> body = new HashMap<>();

        if (adminUpdateReqDTO.getEmail() != null) {
            boolean emailExists = userRepository.existsByEmailAndIdNot(adminUpdateReqDTO.getEmail(), id);
            if (emailExists) {
                body.put("email", "Email is already taken.");
            }
        }

        if (adminUpdateReqDTO.getContactNumber() != null) {
            boolean contactExists = userRepository.existsByContactNumberAndIdNot(adminUpdateReqDTO.getContactNumber(), id);
            if (contactExists) {
                body.put("contactNumber", "Contact number is already taken.");
            }
        }


        if (!body.isEmpty()) {
            throw new DuplicateFieldException(body, "Duplicated unique values", false);
        }

        if (adminUpdateReqDTO.getFirstName() != null) {
            u.setFirstName(adminUpdateReqDTO.getFirstName());
            u.setName(adminUpdateReqDTO.getFirstName() + " " +
                    (adminUpdateReqDTO.getLastName() != null ? adminUpdateReqDTO.getLastName() : u.getLastName()));
        }

        if (adminUpdateReqDTO.getLastName() != null) {
            u.setLastName(adminUpdateReqDTO.getLastName());
            u.setName((adminUpdateReqDTO.getFirstName() != null ? adminUpdateReqDTO.getFirstName() : u.getFirstName())
                    + " " + adminUpdateReqDTO.getLastName());
        }

        if (adminUpdateReqDTO.getEmail() != null) {
            u.setEmail(adminUpdateReqDTO.getEmail());
        }

        if (adminUpdateReqDTO.getContactNumber() != null) {
            u.setContactNumber(adminUpdateReqDTO.getContactNumber());
        }

        if (adminUpdateReqDTO.getStatus() != null) {
            u.setStatus(adminUpdateReqDTO.getStatus());
        }

        userRepository.save(u);

        return new SuccessDTO("Admin updated successfully.", true);
    }


    public SuccessDTO deleteAdmin(Integer id){
        User u = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Map.of(
                        "userId", "No user found with ID " + id),
                        "User not found.",
                        false)
                );
        userRepository.delete(u);
        return new SuccessDTO("Admin delete successfully.", true);
    }
}
