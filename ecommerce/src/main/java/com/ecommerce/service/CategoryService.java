package com.ecommerce.service;

import com.ecommerce.dto.request.CategoryDTO;
import com.ecommerce.dto.response.MessageResponseDTO;
import com.ecommerce.exception.CategoryNameAlreadyExistsException;
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

    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws CategoryNameAlreadyExistsException {
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

    private void verifyIfIsAlreadyRegistered(String name) throws CategoryNameAlreadyExistsException {
        Optional<Category> optSavedCategory = categoryRepository.findByCategoryName(name);
        if (optSavedCategory.isPresent()) {
            throw new CategoryNameAlreadyExistsException(name);
        }
    }

    private Category verifyIfExists(String name) throws CategoryNotFoundException {
        return categoryRepository.findByCategoryName(name)
                .orElseThrow(() -> new CategoryNotFoundException(name));
    }

    public MessageResponseDTO update(String name, CategoryDTO categoryDTO) throws CategoryNotFoundException, CategoryNameAlreadyExistsException {
        verifyIfExists(name);
        verifyIfIsAlreadyRegistered(categoryDTO.getCategoryName());
        Category updatedCategory = categoryMapper.toModel(categoryDTO);
        Category savedCategory = categoryRepository.save(updatedCategory);
        return createMessageResponse("Category successfully updated with ID ", savedCategory.getId());
    }

    private MessageResponseDTO createMessageResponse(String s, Long id2) {
        return MessageResponseDTO.builder()
                .message(s + id2)
                .build();
    }
}
