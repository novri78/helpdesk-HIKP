package com.example.helpdesk.backend.mapper;

import com.example.helpdesk.backend.constant.Department;
import com.example.helpdesk.backend.constant.Role;
import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    // Convert User to UserDTO
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName (user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(Role.valueOf (user.getRole ().name ()));
        userDTO.setDepartment(Department.valueOf (user.getDepartment ().name ()));
        userDTO.setPhoneNumber(user.getPhoneNumber());
        return userDTO;
    }

    // Convert UserDTO to User
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setName (userDTO.getName ());
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

    public void updateEntity(UserDTO userDTO, User user) {
        if (userDTO != null && user != null) {
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getName());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            user.setDepartment(userDTO.getDepartment());
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
    }
}
