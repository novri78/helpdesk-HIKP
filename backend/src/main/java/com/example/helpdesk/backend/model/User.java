package com.example.helpdesk.backend.model;

import com.example.helpdesk.backend.constant.Department;
import com.example.helpdesk.backend.constant.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "user")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(nullable = false)
    @JsonProperty("full_name")
    private String fullName;

    @Column(nullable = false)
    @JsonProperty("password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("department")
    private Department department;

    @Column(nullable = false)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

}
