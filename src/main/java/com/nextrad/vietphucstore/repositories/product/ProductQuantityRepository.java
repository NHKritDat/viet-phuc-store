package com.nextrad.vietphucstore.repositories.product;

import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, UUID> {
    Optional<ProductQuantity> findByIdAndDeleted(UUID id, boolean deleted);

    ProductQuantity findByProduct_IdAndDeleted(UUID id, boolean deleted);
}