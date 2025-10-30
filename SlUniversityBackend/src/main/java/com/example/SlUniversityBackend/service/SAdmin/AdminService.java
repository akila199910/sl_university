package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.config.security.Roles;
import com.example.SlUniversityBackend.dto.Admin.users.AdminReqDTO;
import com.example.SlUniversityBackend.dto.Admin.users.SystemUserPageDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import com.example.SlUniversityBackend.entity.Role;
import com.example.SlUniversityBackend.entity.User;
import com.example.SlUniversityBackend.entity.UserProfile;
import com.example.SlUniversityBackend.exception.DuplicateFieldException;
import com.example.SlUniversityBackend.exception.NotFoundException;
import com.example.SlUniversityBackend.repository.RoleRepository;
import com.example.SlUniversityBackend.repository.UserProfileRepository;
import com.example.SlUniversityBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PagedResourcesAssembler<UserResponseDTO> pagedResourcesAssembler;

    public SuccessDTO getSystemUsers(Pageable pageable, String search, String role){
        Page<User> users;
        boolean hasSearch = search != null && !search.trim().isEmpty();
        boolean hasRole = role != null && !role.trim().isEmpty();
        System.out.println(hasRole);

        if (hasSearch && hasRole) {
            // Only users whose name contains search AND has the selected role, excluding ROLE_USER
            users = userRepository.findByNameContainingIgnoreCaseAndRolesNotContaining(
                    search,
                    roleRepository.findById(Integer.parseInt(role)),
                    roleRepository.findByName(Roles.ROLE_USER),
                    pageable
            );
        } else if (hasSearch) {
            // Only users whose name contains search, excluding ROLE_USER
            users = userRepository.findByNameContainingIgnoreCaseAndRolesNotContaining(
                    search,
                    roleRepository.findByName(Roles.ROLE_USER),
                    pageable
            );
        } else if (hasRole) {
            // Only users with the selected role, excluding ROLE_USER
            users = userRepository.findByRoles(
                    roleRepository.findById(Integer.parseInt(role)),
                    pageable
            );
        } else {
            // All users excluding ROLE_USER
            users = userRepository.findByRolesNotContaining(
                    roleRepository.findByName(Roles.ROLE_USER),
                    pageable);
        }

        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (User u : users) {
            UserResponseDTO userDto = new UserResponseDTO();
            userDto.setId(u.getId());
            userDto.setFirstName(u.getFirstName());
            userDto.setLastName(u.getLastName());
            userDto.setName(u.getName());
            userDto.setEmail(u.getEmail());
            userDto.setContactNumber(u.getContactNumber());
            userDto.setStatus(u.getStatus());
            userDto.setProfile(new UserResponseDTO.ProfileDTO(
                    u.getProfile().getId(),
                    u.getProfile().getProfileUrl(),
                    u.getProfile().getCoverUrl()
            ));
            userDto.setRoles(getListOfRoles(u));
            userResponseDTOList.add(userDto);
        }

        Page<UserResponseDTO> userResponsePage =
                new PageImpl<>(userResponseDTOList, pageable, users.getTotalElements());

        PagedModel<EntityModel<UserResponseDTO>> pagedModel =
                pagedResourcesAssembler.toModel(userResponsePage);


        List<Role> roleList = roleRepository.findAll();
        List<UserResponseDTO.RoleDTO> roleDtoList = new ArrayList<>();
        roleList.forEach(r -> roleDtoList.add(new UserResponseDTO.RoleDTO(r.getId(), r.getName())));

        SystemUserPageDTO systemUserPageDTO = new SystemUserPageDTO(pagedModel, roleDtoList);


        return new SuccessDTO("System Users list",true, systemUserPageDTO);
    }


    public SuccessDTO createSystemUser(AdminReqDTO adminReqDTO){

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

        user.setPassword(passwordEncoder.encode(!adminReqDTO.getPassword().isEmpty() ? adminReqDTO.getPassword() : "Admin@1234"));
        Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);
        user.setProfile(userProfile);
        userRepository.save(user);

       return new SuccessDTO("System users created success.",true, null);
    }

    public SuccessDTO getSystemUserById(Integer id){

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
            userDto.setRoles(getListOfRoles(u));
            userDto.setProfile(new UserResponseDTO.ProfileDTO(u.getProfile().getId(),u.getProfile().getProfileUrl(),u.getProfile().getCoverUrl()));
            return new SuccessDTO("User fletch successfully", true, userDto);
    }

    private List<UserResponseDTO.RoleDTO> getListOfRoles(User u){
        if(u.getRoles() != null && !u.getRoles().isEmpty()){

            return u.getRoles().stream()
                    .map(role -> new UserResponseDTO.RoleDTO(role.getId(),role.getName()))
                    .collect(Collectors.toList());

        }else {
           return List.of();
        }
    }

//    public SuccessDTO updateAdmin(Integer id, AdminUpdateReqDTO adminUpdateReqDTO){
//        User u = userRepository.findById(id)
//                .orElseThrow(()-> new NotFoundException(Map.of(
//                        "userId", "No user found with ID " + id),
//                        "User not found.",
//                        false)
//                );
//
//        Map<String, String> body = new HashMap<>();
//
//        if (adminUpdateReqDTO.getEmail() != null) {
//            boolean emailExists = userRepository.existsByEmailAndIdNot(adminUpdateReqDTO.getEmail(), id);
//            if (emailExists) {
//                body.put("email", "Email is already taken.");
//            }
//        }
//
//        if (adminUpdateReqDTO.getContactNumber() != null) {
//            boolean contactExists = userRepository.existsByContactNumberAndIdNot(adminUpdateReqDTO.getContactNumber(), id);
//            if (contactExists) {
//                body.put("contactNumber", "Contact number is already taken.");
//            }
//        }
//
//
//        if (!body.isEmpty()) {
//            throw new DuplicateFieldException(body, "Duplicated unique values", false);
//        }
//
//        if (adminUpdateReqDTO.getFirstName() != null) {
//            u.setFirstName(adminUpdateReqDTO.getFirstName());
//            u.setName(adminUpdateReqDTO.getFirstName() + " " +
//                    (adminUpdateReqDTO.getLastName() != null ? adminUpdateReqDTO.getLastName() : u.getLastName()));
//        }
//
//        if (adminUpdateReqDTO.getLastName() != null) {
//            u.setLastName(adminUpdateReqDTO.getLastName());
//            u.setName((adminUpdateReqDTO.getFirstName() != null ? adminUpdateReqDTO.getFirstName() : u.getFirstName())
//                    + " " + adminUpdateReqDTO.getLastName());
//        }
//
//        if (adminUpdateReqDTO.getEmail() != null) {
//            u.setEmail(adminUpdateReqDTO.getEmail());
//        }
//
//        if (adminUpdateReqDTO.getContactNumber() != null) {
//            u.setContactNumber(adminUpdateReqDTO.getContactNumber());
//        }
//
//        if (adminUpdateReqDTO.getStatus() != null) {
//            u.setStatus(adminUpdateReqDTO.getStatus());
//        }
//
//        userRepository.save(u);
//
//        return new SuccessDTO("Admin updated successfully.", true, null);
//    }


//    public SuccessDTO deleteAdmin(Integer id){
//        User u = userRepository.findById(id)
//                .orElseThrow(()-> new NotFoundException(Map.of(
//                        "userId", "No user found with ID " + id),
//                        "User not found.",
//                        false)
//                );
//        userRepository.delete(u);
//        return new SuccessDTO("Admin delete successfully.", true, null);
//    }

}
