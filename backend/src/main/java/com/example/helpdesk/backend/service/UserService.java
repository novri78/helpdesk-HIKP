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
        userDTO.setId (user.getId ());
        userDTO.setEmail (user.getEmail ());
        userDTO.setFullName (user.getFullName ());
        userDTO.setPassword (user.getPassword ());
        userDTO.setRole (user.getRole ());
        userDTO.setDepartment (user.getDepartment ());
        userDTO.setPhoneNumber (user.getPhoneNumber ());
        return userDTO;
    }

    // Convert UserDTO to User
    private User convertToEntity(UserDTO userDTO) {
        User user = new User (  );
        user.setId (userDTO.getId ());
        user.setEmail (userDTO.getEmail ());
        user.setFullName (userDTO.getFullName ());
        user.setPassword (userDTO.getPassword ());
        user.setRole (userDTO.getRole ());
        user.setDepartment (userDTO.getDepartment ());
        user.setPhoneNumber (userDTO.getPhoneNumber ());
        return user;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity (userDTO);
        userRepository.save (user);
        return convertToDTO (user);
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
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setPassword (userDetails.getPassword ( ));
        user.setRole (userDetails.getRole ());
        user.setDepartment (userDetails.getDepartment ());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        userRepository.save (user);
        return convertToDTO (user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById (id);
    }

}
