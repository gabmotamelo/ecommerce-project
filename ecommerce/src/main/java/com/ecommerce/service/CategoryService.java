package com.ecommerce.service;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.exception.CategoryAlreadyExistsException;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor= @__(@Autowired))
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws CategoryAlreadyExistsException {
        verifyIfIsAlreadyRegistered(categoryDTO.getCategoryName());
        Category category = categoryMapper.toModel(categoryDTO);
        Category categorySaved = categoryRepository.save(category);
        return categoryMapper.toDTO(categorySaved);
    }

    public List<CategoryDTO> listAll(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO listCategoryByName(String name) throws CategoryNotFoundException {
        Category foundCategory = categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
        return categoryMapper.toDTO(foundCategory);
    }

    private void verifyIfIsAlreadyRegistered(String name) throws CategoryAlreadyExistsException {
        Optional<Category> optSavedCategory = categoryRepository.findByCategoryName(name);
        if (optSavedCategory.isPresent()) {
            throw new CategoryAlreadyExistsException(name);
        }
    }

    private Category verifyIfExists(String name) throws CategoryNotFoundException {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }


}
