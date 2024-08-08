package com.example.helpdesk.backend.dto;

import com.example.helpdesk.backend.constant.Department;
import com.example.helpdesk.backend.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String password;
    private Role role;
    private Department department;
    private String phoneNumber;

    // Tambahkan field token
    private String token;
}
