package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.CategoryDTO;
import com.example.helpdesk.backend.model.Category;
import com.example.helpdesk.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Convert Category to CategoryrDTO
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO ();
        categoryDTO.setId (categoryDTO.getId ( ));
        categoryDTO.setName (categoryDTO.getName ( ));
        categoryDTO.setDescription (categoryDTO.getDescription ( ));
        return categoryDTO;
    }

    // Convert CategoryDTO to Category
    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll ().stream ()
                .map (this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById (id)
                .orElseThrow ( () -> new RuntimeException ( "Category not found" ));
        return convertToDTO (category);
    }

    public CategoryDTO updateCategoryById(Long id, CategoryDTO categoryDetails) {
        Category category = categoryRepository.findById (id)
                .orElseThrow ( () -> new RuntimeException ( "Category not found" ));
        category.setName (categoryDetails.getName ( ));
        category.setDescription (categoryDetails.getDescription ( ));
        categoryRepository.save (category);
        return convertToDTO (category);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = convertToEntity (categoryDTO);
        categoryRepository.save (category);
        return convertToDTO (category);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById (id);
    }
}
