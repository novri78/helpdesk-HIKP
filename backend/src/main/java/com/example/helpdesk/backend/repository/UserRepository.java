package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.constant.Role;
import com.example.helpdesk.backend.dto.UserDTO;
import com.example.helpdesk.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Add this method to check if a user exists by email
    boolean existsByEmail(String email);

    // Method ini hanya mencari pengguna yang tidak dihapus
    List<UserDTO> findAllByIsDeletedFalse();

    // Optional: jika Anda ingin mencari user by id tanpa yang sudah dihapus
    Optional<User> findByIdAndIsDeletedFalse(Long id);

    //Optional find username by the ROLE
    Optional<User> findByIdAndRole(Long id, Role role);

}
