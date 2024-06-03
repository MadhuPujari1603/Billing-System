package com.project.billing.Repository;

import com.project.billing.Dto.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findByNameContaining(String letter);
}