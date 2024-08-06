package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.constant.Department;
import com.example.helpdesk.backend.constant.Role;
import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    // Convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(Role.valueOf (user.getRole ().name ()));
        userDTO.setDepartment(Department.valueOf (user.getDepartment ().name ()));
        userDTO.setPhoneNumber(user.getPhoneNumber());
        return userDTO;
    }

    // Convert UserDTO to User
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setPassword(userDTO.getPassword());
        try{
            user.setRole(Role.valueOf (userDTO.getRole ( ).name ( ).toUpperCase ()));
            user.setDepartment(Department.valueOf(userDTO.getDepartment ().name ().toUpperCase ()));
        }
        catch (IllegalArgumentException e) {
            logger.error ("Invalid enum value for Role or Department", e);
            throw e;
        }
        user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating user with email: {}", userDTO.getEmail());
        User user = convertToEntity (userDTO);
        userRepository.save (user);
        return convertToDTO (user);
    }

    @Transactional
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return  userRepository.findAll ().stream ()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        User user = userRepository.findById (id)
                .orElseThrow(() -> {
                    logger.error("User not found for id: {}", id);
                    return new RuntimeException("User not found for id :: " + id);
                });
       return convertToDTO (user);
    }

    @Transactional
    public UserDTO updateUserById(Long id, UserDTO userDetails) {
        logger.info("Updating user with id: {}", id);
        User user = userRepository.findById (id)
                .orElseThrow(() -> {
                    logger.error("User not found for id: {}", id);
                    return new RuntimeException("User not found for id :: " + id);
                });
        user.setEmail(userDetails.getEmail());
        user.setFullName(userDetails.getFullName());
        user.setPassword (userDetails.getPassword ( ));
        user.setRole (Role.valueOf (userDetails.getRole ().name ().toUpperCase ()));
        user.setDepartment (Department.valueOf (userDetails.getDepartment ().name ().toUpperCase ()));
        user.setPhoneNumber(userDetails.getPhoneNumber());
        userRepository.save (user);
        return convertToDTO (user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById (id);
    }

}
