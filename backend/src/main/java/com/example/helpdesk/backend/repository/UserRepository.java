package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
