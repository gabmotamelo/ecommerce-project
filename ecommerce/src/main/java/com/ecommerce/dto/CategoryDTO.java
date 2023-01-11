package com.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @Schema(description = "Products category Id.")
    private Long id;

    @NotBlank
    @Size(min = 2, max = 40)
    @Schema(description = "Products category name.")
    private String categoryName;

    @NotNull
    @Schema(description = "Product category description.")
    private String categoryDescription;

    @NotBlank
    @Schema(description = "Product category url's image.")
    private String imageUrl;

}
