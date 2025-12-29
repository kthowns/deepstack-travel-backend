package com.example.deepstacktravel.product.service;

import com.example.deepstacktravel.product.dto.ProductResponse;
import com.example.deepstacktravel.product.entity.Category;
import com.example.deepstacktravel.product.entity.Product;
import com.example.deepstacktravel.product.repository.CategoryRepository;
import com.example.deepstacktravel.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> getProducts(String categoryName, Long lastId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<Product> products;

        if (categoryName != null && !categoryName.isEmpty()) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryName));

            if (lastId == null) {
                products = productRepository.findByCategoryOrderByIdAsc(category, pageable);
            } else {
                products = productRepository.findByCategoryAndIdGreaterThanOrderByIdAsc(category, lastId, pageable);
            }
        } else {
            if (lastId == null) {
                products = productRepository.findByOrderByIdAsc(pageable);
            } else {
                products = productRepository.findByIdGreaterThanOrderByIdAsc(lastId, pageable);
            }
        }

        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
        return ProductResponse.fromEntity(product);
    }
}
