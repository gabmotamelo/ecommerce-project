package com.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Products category Id.")
    private Long id;

    @Column(name = "category_name", unique = true)
    @Schema(description = "Products category name.")
    private  String categoryName;

    @Column(name = "category_description")
    @Schema(description = "Product category description.")
    private String categoryDescription;

    @Column(name = "image_url", unique = true)
    @Schema(description = "Product category url's image.")
    private String imageUrl;



}
