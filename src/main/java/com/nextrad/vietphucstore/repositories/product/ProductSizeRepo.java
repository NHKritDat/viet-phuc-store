package com.nextrad.vietphucstore.repositories.product;

import com.nextrad.vietphucstore.entities.product.ProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductSizeRepo extends JpaRepository<ProductSize, UUID> {
    Page<ProductSize> findByDeleted(boolean deleted, Pageable pageable);

    Optional<ProductSize> findByIdAndDeleted(UUID id, boolean deleted);
}