package com.example.SlUniversityBackend.controller.User;

import com.example.SlUniversityBackend.dto.Profile.MyProfileDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.User.MyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
