package com.example.SlUniversityBackend.service.SAdmin;

import com.example.SlUniversityBackend.config.security.Roles;
import com.example.SlUniversityBackend.dto.Admin.Roles.RoleCreatePageDTO;
import com.example.SlUniversityBackend.dto.Admin.Roles.RoleUpdatePageDTO;
import com.example.SlUniversityBackend.dto.Admin.SystemUsers.*;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.dto.User.UserResponseDTO;
import com.example.SlUniversityBackend.entity.Permission;
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

import static com.example.SlUniversityBackend.config.security.Permissions.*;
import static com.example.SlUniversityBackend.config.security.Permissions.ROLE_DELETE;
import static com.example.SlUniversityBackend.config.security.SecurityUtils.checkPermission;

@Service
public class SystemUserService {

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

        if (hasSearch && hasRole) {
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
            users = userRepository.findAll(pageable);
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

            String encodedId = Base64.getUrlEncoder().withoutPadding().encodeToString(String.valueOf(u.getId()).getBytes());
            userDto.setViewUrl(apiBaseUrl + "/users/" + encodedId);
            userDto.setEditUrl(apiBaseUrl + "/users/" + encodedId);
            userDto.setDeleteUrl(apiBaseUrl + "/users/" + encodedId);
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

    public UserCreatePageDTO userCreatePage(){
        List<Role> allRoles = roleRepository.findAll();
        List<UserCreatePageDTO.RoleItem> listOfItem = new ArrayList<>();

        allRoles.forEach(role -> {
            listOfItem.add(new UserCreatePageDTO.RoleItem(role.getId(),role.getName()));

        });

        return new UserCreatePageDTO(listOfItem);
    }

    public SuccessDTO createSystemUser(SystemUserCreateDTO systemUserCreateDTO){

        Map<String, String> body = new HashMap<>();

        if(userRepository.existsByEmail(systemUserCreateDTO.getEmail())){
           body.put("email", "Email is already taken.");
        }
        if(userRepository.existsByContactNumber(systemUserCreateDTO.getContactNumber())){
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
        user.setFirstName(systemUserCreateDTO.getFirstName());
        user.setLastName(systemUserCreateDTO.getLastName());
        user.setName(systemUserCreateDTO.getFirstName() + " " + systemUserCreateDTO.getLastName());
        user.setEmail(systemUserCreateDTO.getEmail());
        user.setContactNumber(systemUserCreateDTO.getContactNumber());
        user.setStatus(systemUserCreateDTO.getStatus() == null || systemUserCreateDTO.getStatus());
        user.setPassword(passwordEncoder.encode("User@1234"));

        Set<Role> roles = new HashSet<>();
        systemUserCreateDTO.getRoles().forEach(id->{
            roles.add(roleRepository.findById(id).get());

        });

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
            return new SuccessDTO("System User fletch successfully", true, userDto);
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

    public UserUpdatePageDTO getUpdatePage(Integer id){

        boolean canUpdate = false;
        boolean checkList =  checkPermission(USER_UPDATE);
        if(checkList){
            canUpdate = true;
        }

        User u = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Map.of(
                        "userId", "No user found with ID " + id),
                        "User not found.",
                        false)
                );

        List<Integer> userRoleIds = u.getRoles().stream()
                .map(Role::getId)
                .toList();

        List<Role> allRoles = roleRepository.findAll();

        List<UserUpdatePageDTO.RoleDTO> roleDTOList = new ArrayList<>();

        allRoles.forEach(role -> {
            boolean select = false;
            if(userRoleIds.contains(role.getId())){
                select = true;
            }
            roleDTOList.add(new UserUpdatePageDTO.RoleDTO(role.getId(),role.getName(),select));
        });



        UserUpdatePageDTO userUpdatePageDTO = new UserUpdatePageDTO();
        userUpdatePageDTO.setContactNumber(u.getContactNumber());
        userUpdatePageDTO.setEmail(u.getEmail());
        userUpdatePageDTO.setId(u.getId());
        userUpdatePageDTO.setFirstName(u.getFirstName());
        userUpdatePageDTO.setLastName(u.getLastName());
        userUpdatePageDTO.setProfile(u.getProfile().getProfileUrl());
        userUpdatePageDTO.setStatus(u.getStatus());
        userUpdatePageDTO.setCanUpdate(canUpdate);
        userUpdatePageDTO.setRoles(roleDTOList);

        return userUpdatePageDTO;

    }

    public SuccessDTO updateSystemUser(Integer id,  SystemUserUpdateDTO systemUserUpdateDTO){

        User u = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Map.of(
                        "userId", "No user found with ID " + id),
                        "User not found.",
                        false)
                );

        if (systemUserUpdateDTO.getStatus() != null) {
            u.setStatus(systemUserUpdateDTO.getStatus());
        }

        List<Integer> roleIdsToSet = systemUserUpdateDTO.getRoleIds();

        Set<Role> updatedRoles = roleIdsToSet.stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

                u.setRoles(updatedRoles);

                userRepository.save(u);

        return new SuccessDTO("System User updated successfully.", true, null);
    }


    public SuccessDTO deleteAdmin(Integer id){
        User u = userRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(Map.of(
                        "userId", "No user found with ID " + id),
                        "User not found.",
                        false)
                );
        userRepository.delete(u);
        return new SuccessDTO("Admin delete successfully.", true, null);
    }

}
