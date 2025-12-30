package com.example.deepstacktravel.product.service;

import com.example.deepstacktravel.product.dto.ProductResponse;
import com.example.deepstacktravel.product.dto.SearchRequest;
import com.example.deepstacktravel.product.entity.Category;
import com.example.deepstacktravel.product.entity.Product;
import com.example.deepstacktravel.product.repository.CategoryRepository;
import com.example.deepstacktravel.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public List<ProductResponse> searchProducts(SearchRequest request) {
        // 1. Validation: 비즈니스 로직 상의 데이터 정합성 체크
        validateSearchRequest(request);

        // 2. 동적 쿼리 실행 (QueryDSL을 쓰지 않는 경우 Specification이나 Stream 활용)
        // 여기서는 개념을 보여주기 위해 Stream을 활용한 필터링 예시를 들지만,
        // 실제 운영 환경에서는 DB 레벨에서 필터링하도록 Repository 메서드를 설계해야 합니다.
        return productRepository.findAll().stream()
                .filter(p -> request.getCategory() == null || p.getCategory().equals(request.getCategory()))
                .filter(p -> request.getKeyword() == null || p.getName().contains(request.getKeyword()))
                .filter(p -> isWithinBudget(p.getPrice(), request.getMinBudget(), request.getMaxBudget()))
                .filter(p -> isWithinDateRange(p.getDate(), request.getStartDate(), request.getEndDate()))
                .map(ProductResponse::fromEntity)
                .toList();
    }

    private void validateSearchRequest(SearchRequest request) {
        // 1. 예산 null 체크: 둘 중 하나만 있는 경우 방지
        boolean hasMinBudget = request.getMinBudget() != null;
        boolean hasMaxBudget = request.getMaxBudget() != null;

        if (hasMinBudget != hasMaxBudget) {
            throw new IllegalArgumentException("최소 예산과 최대 예산은 함께 입력되어야 합니다.");
        }

        // 2. 날짜 null 체크: 둘 중 하나만 있는 경우 방지
        boolean hasStartDate = request.getStartDate() != null;
        boolean hasEndDate = request.getEndDate() != null;

        if (hasStartDate != hasEndDate) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 함께 입력되어야 합니다.");
        }

        // 3. 범위 정합성 체크 (null이 아님을 확인한 후)
        if (hasMinBudget && request.getMinBudget() > request.getMaxBudget()) {
            throw new IllegalArgumentException("최소 예산이 최대 예산보다 클 수 없습니다.");
        }

        if (hasStartDate && request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("시작 날짜가 종료 날짜보다 늦을 수 없습니다.");
        }
    }

    private boolean isWithinBudget(Double price, Double min, Double max) {
        if (min != null && price < min) return false;
        if (max != null && price > max) return false;
        return true;
    }

    private boolean isWithinDateRange(LocalDate target, LocalDate start, LocalDate end) {
        if (start != null && target.isBefore(start)) return false;
        if (end != null && target.isAfter(end)) return false;
        return true;
    }
}
