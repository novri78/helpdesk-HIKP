package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.mapper.UserMapper;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserDTO registerUser(@Valid UserDTO userDTO) {
        logger.info("Registering user with email: {}", userDTO.getEmail());
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO authenticateUser(String email, String password) {
        logger.info("Authenticating user with email: {}", email);
        User user = userRepository.findByEmail (email)
                .orElseThrow(() -> new UsernameNotFoundException ("User not found"));
        if (passwordEncoder.matches(password, user.getPassword())) {
            logger.info("User authenticated successfully: " + email);
            return userMapper.toDTO (user);
        } else {
            logger.error("Invalid password for user: " + email);
            throw new BadCredentialsException ("Invalid password");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: " + email);
                    return new UsernameNotFoundException ("User not found with email: "+email);
                });
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority (user.getRole ().name ()))
        );
    }



    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return  userRepository.findAll ().stream ()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for id :: " + id));
       return userMapper.toDTO (user);
    }

    @Transactional
    public UserDTO updateUserById(Long id, @Valid UserDTO userDetails) {
        logger.info("Updating user with id: {}", id);
        User user = userRepository.findById (id)
                .orElseThrow(() -> new RuntimeException("User not found for id :: " + id));
        userMapper.updateEntity (userDetails, user);
        userRepository.save (user);
        return userMapper.toDTO (user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        logger.info("Deleting user with id: {}", id);
        userRepository.deleteById (id);
    }

}
