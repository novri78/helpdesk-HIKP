package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


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

//    @PostMapping("/me")
//    public ResponseEntity<?> getAuthenticatedUser(@RequestBody UserDTO userDTO) {
//        // Retrieve the authenticated user's email from the SecurityContext
//        // String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        // UserDTO userDTO = userService.findUserByEmail(userEmail);
//        return ResponseEntity.ok(userService.getAuthenticatedUser (userDTO.getToken ()));
//    }
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(Authentication authentication) {
        // Authentication objek ini otomatis diisi oleh JwtRequestFilter jika token valid
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getAuthenticatedUser(email));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Dalam JWT Stateless, kita cukup memberikan respon sukses.
        // Jika ingin lebih advance, masukkan token ini ke Blacklist (Redis).
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful. Token invalidated locally.");
        return ResponseEntity.ok(response);
    }
}
