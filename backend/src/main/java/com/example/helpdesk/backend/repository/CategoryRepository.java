package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
