package com.ecommerce.controller;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.exception.CategoryAlreadyExistsException;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController{

    private final CategoryService categoryService;

    @Operation(summary = "Create category by given name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal server error."),
            @ApiResponse(responseCode = "201", description = "Category create with success.")
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDTO) throws CategoryAlreadyExistsException {
        return categoryService.createCategory(categoryDTO);
    }

    @Operation(summary = "List all categories.")
    @GetMapping()
    public List<CategoryDTO> listAllCategories(){
        return categoryService.listAll();
    }

    @Operation(summary = "Get category by name.")
    @GetMapping("/{name}")
    public CategoryDTO listCategoryByName(@PathVariable String name) throws CategoryNotFoundException {
        return categoryService.listCategoryByName(name);
    }


}


