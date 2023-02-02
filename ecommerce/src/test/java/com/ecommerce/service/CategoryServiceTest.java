package com.ecommerce.service;

import com.ecommerce.builder.CategoryDTOBuilder;
import com.ecommerce.dto.request.CategoryDTO;
import com.ecommerce.dto.response.MessageResponseDTO;
import com.ecommerce.exception.CategoryNameAlreadyExistsException;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void whenPOSTCalledAndCategoryInformedThenItShouldBeCreated() throws CategoryNameAlreadyExistsException {
        // given
        CategoryDTO expectedCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        Category expectedSavedCategory = categoryMapper.toModel(expectedCategoryDTO);

        // when
        when(categoryRepository.findByCategoryName(expectedCategoryDTO.getCategoryName())).thenReturn(Optional.empty());
        when(categoryRepository.save(expectedSavedCategory)).thenReturn(expectedSavedCategory);

        //then
        CategoryDTO createdCategoryDTO = categoryService.createCategory(expectedCategoryDTO);

        assertThat(createdCategoryDTO.getId(), is(equalTo(expectedCategoryDTO.getId())));
        assertThat(createdCategoryDTO.getCategoryName(), is(equalTo(expectedCategoryDTO.getCategoryName())));
        assertThat(createdCategoryDTO.getCategoryDescription(), is(equalTo(expectedCategoryDTO.getCategoryDescription())));
        assertThat(createdCategoryDTO.getImageUrl(),is(equalTo(expectedCategoryDTO.getImageUrl())));

    }

    @Test
    void whenPOSTCalledAndAlreadyRegisteredCategoryInformedThenAnExceptionShouldBeThrown() {
        // given
        CategoryDTO expectedCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        Category duplicatedCategory = categoryMapper.toModel(expectedCategoryDTO);

        // when
        when(categoryRepository.findByCategoryName(expectedCategoryDTO.getCategoryName())).thenReturn(Optional.of(duplicatedCategory));

        // then
        assertThrows(CategoryNameAlreadyExistsException.class, () -> categoryService.createCategory(expectedCategoryDTO));
    }

    @Test
    void whenGETCalledWithValidCategoryNameThenReturnACategory() throws CategoryNotFoundException {
        // given
        CategoryDTO expectedFoundCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        Category expectedFoundCategory = categoryMapper.toModel(expectedFoundCategoryDTO);

        // when
        when(categoryRepository.findByCategoryName(expectedFoundCategory.getCategoryName())).thenReturn(Optional.of(expectedFoundCategory));

        // then
        CategoryDTO foundCategoryDTO = categoryService.listCategoryByName(expectedFoundCategoryDTO.getCategoryName());

        assertThat(foundCategoryDTO, is(equalTo(expectedFoundCategoryDTO)));
    }

    @Test
    void whenGETCalledWithNotRegisteredCategoryNameThenThrowAnException() {
        // given
        CategoryDTO expectedFoundCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();

        // when
        when(categoryRepository.findByCategoryName(expectedFoundCategoryDTO.getCategoryName())).thenReturn(Optional.empty());

        // then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.listCategoryByName(expectedFoundCategoryDTO.getCategoryName()));
    }

    @Test
    void whenGETListCategoryCalledThenReturnAListOfCategories() {
        // given
        CategoryDTO expectedFoundCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        Category expectedFoundCategory = categoryMapper.toModel(expectedFoundCategoryDTO);

        //when
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundCategory));

        //then
        List<CategoryDTO> foundListCategoriesDTO = categoryService.listAll();

        assertThat(foundListCategoriesDTO, is(not(empty())));
        assertThat(foundListCategoriesDTO.get(0), is(equalTo(expectedFoundCategoryDTO)));
    }

    @Test
    void whenGETCalledWithListCategoryThenReturnAnEmptyListOfCategories() {
        //when
        when(categoryRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<CategoryDTO> foundListCategoriesDTO = categoryService.listAll();

        assertThat(foundListCategoriesDTO, is(empty()));
    }

    @Test
    void whenUpdatedIsCalledWithValidIdThenUpdatedCategory() throws CategoryNotFoundException, CategoryNameAlreadyExistsException {
        //given
        CategoryDTO expectedCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        Category expectedSavedCategory = categoryMapper.toModel(expectedCategoryDTO);

        // when
        when(categoryRepository.findByCategoryName(expectedCategoryDTO.getCategoryName())).thenReturn(Optional.of(expectedSavedCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedSavedCategory);


        CategoryDTO categoryDTOToUpdate = CategoryDTOBuilder
                .builder()
                .categoryName("Teste nome atualizado")
                .build()
                .toCategoryDTO();

        // then
        MessageResponseDTO successMessage = categoryService.update(expectedSavedCategory.getCategoryName(), categoryDTOToUpdate);

        assertEquals("Category successfully updated with ID 1", successMessage.getMessage());

    }

    @Test
    void whenUpdatedIsCalledWithInvalidIdThenThrowExceptionCategory(){

        CategoryDTO categoryDTOToUpdate = CategoryDTOBuilder
                .builder()
                .id(2L)
                .categoryName("Teste nome atualizado")
                .build()
                .toCategoryDTO();

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(categoryDTOToUpdate.getCategoryName(), categoryDTOToUpdate),"Category with id 2 not found.");

    }

    @Test
    void whenUpdatedIsCalledWithNameThatAlreadyExistsInTheSystemThenThrowExceptionCategory(){
        String categoryName = "Teste";
        //given
        CategoryDTO expectedCategoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        Category expectedSavedCategory = categoryMapper.toModel(expectedCategoryDTO);

        // when
        when(categoryRepository.findByCategoryName(expectedCategoryDTO.getCategoryName())).thenReturn(Optional.of(expectedSavedCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(expectedSavedCategory);


        CategoryDTO categoryDTOToUpdate = CategoryDTOBuilder
                .builder()
                .categoryName(categoryName)
                .build()
                .toCategoryDTO();

        assertThrows(CategoryNameAlreadyExistsException.class, () -> categoryService.update(categoryDTOToUpdate.getCategoryName(), categoryDTOToUpdate));


    }
}
