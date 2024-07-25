package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.Category;
import com.example.helpdesk.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll ();
    }

    public Category getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById (id);
        if(category.isPresent ()) {
            return category.get ();
        } else {
            throw new RuntimeException("User not found for id :: " + id);
        }
    }

    public Category createCategory(Category category) {
        return categoryRepository.save (category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById (id);
    }
}
