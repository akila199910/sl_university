package com.example.SlUniversityBackend.controller.Admin;

import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.Admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<SuccessDTO> getUsers(@RequestParam(required = false, defaultValue = "0") int pageNo,
                                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                               @RequestParam(required = false, defaultValue = "DECE") String sortDir,
                                               @RequestParam(required = false) String search) {
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        return ResponseEntity.ok(userService.getUsers(PageRequest.of(pageNo,pageSize,sort),search));
    }
}
