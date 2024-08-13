package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return ResponseEntity.ok("User approved successfully");
    }

    @PostMapping
    public ResponseEntity<UserDTO> createNewUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUser = userService.createNewUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return Collections.unmodifiableList (userService.getAllUsers ( ));
    }

    @GetMapping(value = "/{id}")
    public  UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById (id);
    }

    @PutMapping(value = "/{id}")
    public UserDTO updateUserById(@PathVariable Long id, @RequestBody UserDTO userDetails) {
        return userService.updateUserById(id, userDetails);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById (id);
    }
}
