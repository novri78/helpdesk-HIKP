package com.example.helpdesk.backend.dto;

import com.example.helpdesk.backend.util.Department;
import com.example.helpdesk.backend.util.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String password;
    private Role role;
    private Department department;
    private String phoneNumber;
}
