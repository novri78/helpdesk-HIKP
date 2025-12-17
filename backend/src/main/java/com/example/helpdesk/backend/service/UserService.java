package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.mapper.UserMapper;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.UserRepository;
import com.example.helpdesk.backend.util.JwtTokenUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Helper method to validate password strength
    private void validatePasswordStrength(String password) {
        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[A-Za-z].*")) {
            throw new RuntimeException("Password must be at least 8 characters long, contain at least one digit, and one letter");
        }
    }

    @Transactional
    public UserDTO registerUser(@Valid UserDTO userDTO) {
        logger.info("Registering user with email: {}", userDTO.getEmail());

        // Check if the email is already in use
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("Email already in use: {}", userDTO.getEmail());
            throw new RuntimeException("Email is already in use");
        }
        userDTO.setEmail(userDTO.getEmail());
        // Validate the password strength
        validatePasswordStrength(userDTO.getPassword());
        // Encode the password before saving
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setRole (userDTO.getRole ());
        userDTO.setDepartment (userDTO.getDepartment ());
        userDTO.setIsApproved (false);

        // Convert DTO to entity and save the user
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);

        // Generate JWT token
        // String token = jwtTokenUtil.generateToken(user);

        // Return the saved user as DTO
        return userMapper.toDTO(user);
    }

    @Transactional
    public void approveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsApproved(true);
        userRepository.save(user);
        // Tambahkan log atau lakukan query langsung ke database untuk memverifikasi
        userRepository.findById (id).ifPresent (updatedUser -> System.out.println ("User isApproved after save: "+updatedUser.getIsApproved ( )));
    }



    // Method to authenticate a user
    @Transactional(readOnly = true)
    public UserDTO authenticateUser(String email, String password) {
        logger.info("Authenticating user with email: {}", email);

        // Find user by email
        //User user = userRepository.findByEmail (email)
        //        .orElseThrow(() -> new UsernameNotFoundException ("User not found"));

        if (jwtTokenUtil == null) {
            throw new IllegalStateException("JwtTokenUtil is NULL - bean not injected");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Password");
        }

        String token = jwtTokenUtil.generateToken(user);

        UserDTO dto = userMapper.toDTO(user);
        dto.setToken(token);
        return dto;

        // Check if the provided password matches the stored password
//        if (passwordEncoder.matches(password, user.getPassword())) {
//            logger.info("User authenticated successfully: " + email);
//
//            // Generate JWT token and set it to the UserDTO
//            String token = jwtTokenUtil.generateToken (user);
//            UserDTO userDTO = userMapper.toDTO (user);
//            userDTO.setToken (token);               // Set token ke UserDTO
//            return userDTO;
//        } else {
//            logger.error("Invalid password for user: " + email);
//            throw new BadCredentialsException ("Invalid password");
//        }
    }

    // Method to load user by username (used by Spring Security)
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepository.findByEmail(email)
//                .map(user -> {
//                    logger.info("Loading user by email: {}", email);
//                    return new org.springframework.security.core.userdetails.User(
//                            user.getEmail(),
//                            user.getPassword(),
//                            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
//                    );
//                })
//                .orElseThrow(() -> {
//                    logger.error("User not found with email: {}", email);
//                    return new UsernameNotFoundException("User not found with email: " + email);
//                });
//    }

    // Method to get authenticated user from token
    @Transactional(readOnly = true)
    public UserDTO getAuthenticatedUser(String token) {
        logger.info("Getting authenticated user from token");
        String email = jwtTokenUtil.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO createNewUser(@Valid UserDTO userDTO) {
        logger.info("Creating new user with email: {}", userDTO.getEmail());

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            logger.error("Email already in use: {}", userDTO.getEmail());
            throw new RuntimeException("Email is already in use");
        }

        // Validate the password strength
        validatePasswordStrength(userDTO.getPassword());

        // Encode the password before saving
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setIsApproved (true); // Automatically approve new user

        // Convert DTO to entity and save the user
        User user = userMapper.toEntity(userDTO);
        userRepository.save(user);

        return userMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return  userRepository.findAll ().stream ()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllActiveUsers() {
        return userRepository.findAllByIsDeletedFalse();
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

        // Cek apakah password diinput baru
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        } else {
            // Jika password tidak diubah, gunakan password lama
            userDetails.setPassword(user.getPassword());
        }
        userMapper.updateEntity (userDetails, user);
        userRepository.save (user);
        return userMapper.toDTO (user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        logger.info("Soft deleting user with id: {}", id);
        // Get user by ID
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found with id " + id));

        // Run soft delete
        user.setIsDeleted (true);
        userRepository.save(user);
    }

}
