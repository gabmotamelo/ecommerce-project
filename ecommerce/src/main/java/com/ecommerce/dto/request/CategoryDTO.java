package com.ecommerce.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    @Schema(description = "Products category Id.")
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Size(min = 2, max = 40)
    @Schema(description = "Products category name.")
    private String categoryName;

    @NotNull
    @Schema(description = "Product category description.")
    private String categoryDescription;

    @NotNull
    @Schema(description = "Product category url's image.")
    private String imageUrl;

}
