package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.model.AuthenticationRequest;
import com.example.helpdesk.backend.model.AuthenticationResponse;
import com.example.helpdesk.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail (), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail ());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse (jwt));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails);
    }
}
