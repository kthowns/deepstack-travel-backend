package com.example.deepstacktravel.product.repository;

import com.example.deepstacktravel.product.entity.Category;
import com.example.deepstacktravel.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryAndIdGreaterThanOrderByIdAsc(Category category, Long lastId, Pageable pageable);
    List<Product> findByIdGreaterThanOrderByIdAsc(Long lastId, Pageable pageable);
    List<Product> findByCategoryOrderByIdAsc(Category category, Pageable pageable);
    List<Product> findByOrderByIdAsc(Pageable pageable);
}
