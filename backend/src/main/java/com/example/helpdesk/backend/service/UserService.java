package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setDepartment (user.getDepartment ());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName (user.getFullName ());
        userDTO.setPhoneNumber (user.getPhoneNumber ());
        userDTO.setPosition (user.getPosition ());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    // Convert UserDTO to User
    private User convertToEntity(UserDTO userDTO) {
        User user = new User (  );
        user.setId (userDTO.getId ());
        user.setDepartment (userDTO.getDepartment ());
        user.setEmail (userDTO.getEmail ());
        user.setFullName (userDTO.getFullName ());
        user.setPhoneNumber (userDTO.getPhoneNumber ());
        user.setPosition (userDTO.getPosition ());
        user.setUsername (userDTO.getUsername ());
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
        user.setDepartment(userDetails.getDepartment());
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setPosition(userDetails.getPosition());
        user.setUsername(userDetails.getUsername());
        userRepository.save (user);
        return convertToDTO (user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity (userDTO);
        userRepository.save (user);
        return convertToDTO (user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById (id);
    }

}
