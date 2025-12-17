package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok ( userService.registerUser (userDTO) );
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("Request body is null");
        }

        return ResponseEntity.ok(
                userService.authenticateUser(
                        userDTO.getEmail(),
                        userDTO.getPassword()
                )
        );
    }
//    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
//        return ResponseEntity.ok ( userService.authenticateUser (userDTO.getEmail (), userDTO.getPassword ()) );
//    }

    @PostMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(@RequestBody UserDTO userDTO) {
        // Retrieve the authenticated user's email from the SecurityContext
        // String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // UserDTO userDTO = userService.findUserByEmail(userEmail);
        return ResponseEntity.ok(userService.getAuthenticatedUser (userDTO.getToken ()));
    }
}
