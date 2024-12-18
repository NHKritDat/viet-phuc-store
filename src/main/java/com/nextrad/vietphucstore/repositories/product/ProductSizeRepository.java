package com.nextrad.vietphucstore.repositories.product;

import com.nextrad.vietphucstore.entities.product.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, UUID> {
}