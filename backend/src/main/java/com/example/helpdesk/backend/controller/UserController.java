package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@NoArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        System.out.println ("Received user :" + userDTO );
        return userService.createUser (userDTO);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers ();
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
