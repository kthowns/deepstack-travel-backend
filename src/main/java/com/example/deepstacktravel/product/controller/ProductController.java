package com.example.deepstacktravel.product.controller;

import com.example.deepstacktravel.product.dto.CategoryResponse;
import com.example.deepstacktravel.product.dto.ProductResponse;
import com.example.deepstacktravel.product.dto.SearchRequest;
import com.example.deepstacktravel.product.service.CategoryService;
import com.example.deepstacktravel.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Operation(summary = "상품 목록 조회", description = "카테고리별 상품 목록을 페이지네이션으로 조회합니다. lastId를 사용하여 다음 페이지를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {
        List<ProductResponse> products = productService.getProducts(category, lastId, size);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "상품 검색", description = "각 파라미터를 기준으로 상품을 검색")
    @GetMapping("/api/products/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(SearchRequest searchRequest) {
        List<ProductResponse> products = productService.searchProducts(searchRequest);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "카테고리 목록 조회", description = "모든 상품 카테고리 목록을 조회합니다.")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "상품 상세 조회", description = "특정 상품의 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }
}
