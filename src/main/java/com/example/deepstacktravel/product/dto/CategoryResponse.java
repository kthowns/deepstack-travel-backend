package com.example.deepstacktravel.product.dto;

import com.example.deepstacktravel.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;

    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
