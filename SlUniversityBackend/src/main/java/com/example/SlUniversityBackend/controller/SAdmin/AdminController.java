package com.example.SlUniversityBackend.controller.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.users.AdminReqDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.SAdmin.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<SuccessDTO> getSystemUsers(@RequestParam(required = false, defaultValue = "0") int page,
                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                     @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                     @RequestParam(required = false, defaultValue = "ASC") String sortDir,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(required = false) String role)
    {
        Sort sort;
        if(sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())){
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(adminService.getSystemUsers(pageable,search, role));
    }

    @PostMapping
    public ResponseEntity<SuccessDTO> createSystemUser(@Valid @RequestBody AdminReqDTO adminReqDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.createSystemUser(adminReqDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessDTO> getSystemUserById(@PathVariable Integer id){
        return ResponseEntity.ok(adminService.getSystemUserById(id));
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<SuccessDTO> updateAdmin(@PathVariable Integer id, @Valid @RequestBody AdminUpdateReqDTO adminUpdateReqDTO){
//        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.updateAdmin(id, adminUpdateReqDTO));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<SuccessDTO> deleteAdmin(@PathVariable Integer id){
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(adminService.deleteAdmin(id));
//    }
}
