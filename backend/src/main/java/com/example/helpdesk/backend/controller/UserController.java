package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.model.User;
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

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers ();
    }

    @PutMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById (id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println ("Received user :" + user );
        return userService.createUser (user);
    }

    @DeleteMapping
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById (id);
    }
}
