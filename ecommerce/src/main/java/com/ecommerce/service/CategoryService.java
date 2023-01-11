package com.ecommerce.service;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor= @__(@Autowired))
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        Category category = categoryMapper.toModel(categoryDTO);
        Category categorySaved = categoryRepository.save(category);
        return categoryMapper.toDTO(categorySaved);
    }


}
