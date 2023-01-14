package com.ecommerce.builder;

import com.ecommerce.dto.CategoryDTO;
import lombok.Builder;

@Builder
public class CategoryDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String categoryName = "Soccer";

    @Builder.Default
    private String categoryDescription = "Soccer products";

    @Builder.Default
    private String imageUrl = "www.test.com";

    public CategoryDTO toCategoryDTO(){
        return new CategoryDTO(id,
                categoryName,
                categoryDescription,
                imageUrl);
    }

}
