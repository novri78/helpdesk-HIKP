package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.CategoryDTO;
import com.example.helpdesk.backend.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@NoArgsConstructor
@Service
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories ();
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById (id);
    }

    @PutMapping(value = "/{id}")
    public CategoryDTO updateCategoryById(@PathVariable Long id, @RequestBody CategoryDTO categoryDetails) {
        return categoryService.updateCategoryById (id, categoryDetails);
    }

    @PostMapping
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        return  categoryService.createCategory (categoryDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCategoryById(Long id) {
        categoryService.deleteCategoryById (id);
    }


}
