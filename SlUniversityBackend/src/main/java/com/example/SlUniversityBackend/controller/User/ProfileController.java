package com.example.SlUniversityBackend.controller.User;

import com.example.SlUniversityBackend.dto.Profile.MyProfileDTO;
import com.example.SlUniversityBackend.dto.Profile.ProfileUpdateDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.User.MyProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    MyProfileService myProfileService;

    @GetMapping("/my-profile")
    public SuccessDTO getMyProfile(){

        MyProfileDTO myProfileDTO = myProfileService.getMyProfile();

        if(myProfileDTO == null){
            return new SuccessDTO("Login again.", false, null);
        }

        return ResponseEntity.ok(new SuccessDTO("User data fletch", true, myProfileDTO)).getBody();

    }

    @PostMapping("/my-profile")
    public SuccessDTO saveProfileData(@RequestBody @Valid ProfileUpdateDTO profileUpdateDTO){

        return new SuccessDTO("Profile Updated.",true,myProfileService.updateProfileData(profileUpdateDTO));
    }

}
