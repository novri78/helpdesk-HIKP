package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return  userRepository.findAll ();
    }

    public User getUserById(Long id) {
       Optional<User> user = userRepository.findById (id);
       if (user.isPresent ()) {
           return user.get ();
       } else {
           throw new RuntimeException("User not found for id :: " + id);
       }
    }

    public User createUser(User user) {
        return userRepository.save (user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById (id);
    }

}
