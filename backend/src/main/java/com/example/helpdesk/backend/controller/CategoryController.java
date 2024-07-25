package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.model.Category;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories ();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(Long id) {
        return categoryService.getCategoryById (id);
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return  categoryService.createCategory (category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(Long id) {
        categoryService.deleteCategoryById (id);
    }


}
