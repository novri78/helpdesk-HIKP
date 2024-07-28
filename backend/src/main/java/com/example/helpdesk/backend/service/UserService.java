package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName (user.getFullName ());
        userDTO.setPosition (user.getPosition ());
        userDTO.setDepartment (user.getDepartment ());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber (user.getPhoneNumber ());
        return userDTO;
    }

    // Convert UserDTO to User
    private User converToEntity(UserDTO userDTO) {
        User user = new User (  );
        user.setId (userDTO.getId ());
        user.setUsername (userDTO.getUsername ());
        user.setFullName (userDTO.getFullName ());
        user.setPosition (userDTO.getPosition ());
        user.setDepartment (userDTO.getDepartment ());
        user.setEmail (userDTO.getEmail ());
        user.setEmail (userDTO.getEmail ());
        return user;
    }

    public List<UserDTO> getAllUsers() {
        return  userRepository.findAll ().stream ()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
       User user = userRepository.findById (id)
               .orElseThrow (() -> new RuntimeException ( "User not found for id :: " + id ));
       return convertToDTO (user);
    }

    public UserDTO updateUserById(Long id, UserDTO userDetails) {
        User user = userRepository.findById (id)
                .orElseThrow (() -> new RuntimeException ( "User not found for id :: " + id  ));
        user.setUsername(userDetails.getUsername());
        user.setFullName(userDetails.getFullName());
        user.setPosition(userDetails.getPosition());
        user.setDepartment(userDetails.getDepartment());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        userRepository.save (user);
        return convertToDTO (user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = converToEntity (userDTO);
        userRepository.save (user);
        return convertToDTO (user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById (id);
    }

}
