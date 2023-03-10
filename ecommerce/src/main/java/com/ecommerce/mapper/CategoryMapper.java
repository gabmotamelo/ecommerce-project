package com.ecommerce.mapper;

import com.ecommerce.dto.request.CategoryDTO;
import com.ecommerce.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toModel(CategoryDTO categoryDTO);

    CategoryDTO toDTO(Category category);
}
