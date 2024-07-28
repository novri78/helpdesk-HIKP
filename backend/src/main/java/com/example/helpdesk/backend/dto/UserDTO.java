package com.example.helpdesk.backend.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String position;
    private String department;
    private String email;
    private String phoneNumber;
}
