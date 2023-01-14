package com.ecommerce.controller;

import com.ecommerce.builder.CategoryDTOBuilder;
import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static com.ecommerce.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenACategoryIsCreated() throws Exception {
        // given
        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();

        // when
        when(categoryService.createCategory(categoryDTO)).thenReturn(categoryDTO);

        // then
        mockMvc.perform(post("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName", is(categoryDTO.getCategoryName())))
                .andExpect(jsonPath("$.categoryDescription", is(categoryDTO.getCategoryDescription())))
                .andExpect(jsonPath("$.imageUrl", is(categoryDTO.getImageUrl())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        categoryDTO.setCategoryName(null);

        // then
        mockMvc.perform(post("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();

        //when
        when(categoryService.listCategoryByName(categoryDTO.getCategoryName())).thenReturn(categoryDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/category/" + categoryDTO.getCategoryName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName", is(categoryDTO.getCategoryName())))
                .andExpect(jsonPath("$.categoryDescription", is(categoryDTO.getCategoryDescription())))
                .andExpect(jsonPath("$.imageUrl", is(categoryDTO.getImageUrl())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();

        //when
        when(categoryService.listCategoryByName(categoryDTO.getCategoryName())).thenThrow(CategoryNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/category/" + categoryDTO.getCategoryName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETCategoriesListIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();

        //when
        when(categoryService.listAll()).thenReturn(Collections.singletonList(categoryDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/category")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName", is(categoryDTO.getCategoryName())))
                .andExpect(jsonPath("$[0].categoryDescription", is(categoryDTO.getCategoryDescription())))
                .andExpect(jsonPath("$[0].imageUrl", is(categoryDTO.getImageUrl())));
    }
}