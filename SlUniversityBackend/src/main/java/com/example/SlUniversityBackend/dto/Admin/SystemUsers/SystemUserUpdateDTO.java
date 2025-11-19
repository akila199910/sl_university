package com.example.SlUniversityBackend.dto.Admin.SystemUsers;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SystemUserUpdateDTO {

    private List<Integer> roleIds;
    private Boolean status;

}
