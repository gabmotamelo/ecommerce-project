package com.ecommerce.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Products category Id.")
    private Long id;

    @Column(name = "category_name", unique = true, nullable = false)
    @Schema(description = "Products category name.")
    private  String categoryName;

    @Column(name = "category_description", nullable = false)
    @Schema(description = "Product category description.")
    private String categoryDescription;

    @Column(name = "image_url", unique = true, nullable = false)
    @Schema(description = "Product category url's image.")
    private String imageUrl;



}
