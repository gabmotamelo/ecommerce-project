package com.ecommerce.controller;

import com.ecommerce.builder.CategoryDTOBuilder;
import com.ecommerce.dto.request.CategoryDTO;
import com.ecommerce.dto.response.MessageResponseDTO;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.service.CategoryService;
import com.ecommerce.utils.JsonConvertionUtils;
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

    private static final String CATEGORY_API_URL_PATH = "/category";

    private static final String VALID_CATEGORY_NAME = "Soccer Updated";

    private static final long VALID_CATEGORY_ID = 1L;

    private static final String CATEGORY_API_SUBPATH_UPDATE_URL = "/update";

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

    @Test
    void whenGETListWithoutCategoriesIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();

        //when
        when(categoryService.listAll()).thenReturn(Collections.singletonList(categoryDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/category")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenUPDATEIsCalledToThenOKStatusIsReturned() throws Exception {
        CategoryDTO categoryDTOToUpdate = CategoryDTO
                .builder()
                .categoryName(VALID_CATEGORY_NAME)
                .build();

        CategoryDTO categoryDTO = CategoryDTOBuilder.builder().build().toCategoryDTO();
        categoryDTO.setCategoryName(categoryDTOToUpdate.getCategoryName());

        MessageResponseDTO  messageResponseDTO = MessageResponseDTO.builder().build();

        when(categoryService.update(VALID_CATEGORY_NAME, categoryDTO)).thenReturn(messageResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(CATEGORY_API_URL_PATH + "/" + VALID_CATEGORY_NAME + CATEGORY_API_SUBPATH_UPDATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConvertionUtils.asJsonString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(messageResponseDTO.getMessage())));
    }

    @Test
    void whenUPDATEIsCalledWithNonExistentNameThenNotFoundExceptionStatusIsReturned() throws Exception {
        CategoryDTO categoryDTOToUpdate = CategoryDTOBuilder
                .builder()
                .id(2L)
                .categoryName("Teste nome atualizado")
                .build()
                .toCategoryDTO();

        when(categoryService.update(categoryDTOToUpdate.getCategoryName(), categoryDTOToUpdate)).thenThrow(CategoryNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(CATEGORY_API_URL_PATH + "/" + categoryDTOToUpdate.getCategoryName() + CATEGORY_API_SUBPATH_UPDATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConvertionUtils.asJsonString(categoryDTOToUpdate)))
                .andExpect(status().isNotFound());
    }

}