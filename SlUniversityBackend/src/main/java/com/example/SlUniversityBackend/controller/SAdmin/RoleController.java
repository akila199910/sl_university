package com.example.SlUniversityBackend.controller.SAdmin;

import com.example.SlUniversityBackend.dto.Admin.Roles.RoleCreateDTO;
import com.example.SlUniversityBackend.dto.SuccessDTO;
import com.example.SlUniversityBackend.service.SAdmin.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<SuccessDTO> getAllRoles(@RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                  @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                  @RequestParam(required = false, defaultValue = "ASC") String sortDir)
    {

        Sort sort;
        if(sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())){
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(roleService.getAllRoles(pageable));

    }

    @PostMapping
    public ResponseEntity<SuccessDTO> createRole(@Valid @RequestBody RoleCreateDTO roleCreateDTO){
        return ResponseEntity.ok(roleService.roleCreate(roleCreateDTO));
    }


    @GetMapping("/{id}")
    public ResponseEntity<SuccessDTO> getRoleById(@PathVariable Integer id){
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
}
