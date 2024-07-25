package com.example.helpdesk.backend.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String fullName;
    private String position;
    private String department;
    private String email;
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private Set<Ticket> tickets;
}
