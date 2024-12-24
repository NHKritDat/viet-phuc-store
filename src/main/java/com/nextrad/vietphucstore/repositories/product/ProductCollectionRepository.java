package com.nextrad.vietphucstore.repositories.product;

import com.nextrad.vietphucstore.entities.product.ProductCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollection, UUID> {
    Optional<ProductCollection> findByIdAndDeleted(UUID id, boolean deleted);

    Page<ProductCollection> findByDeleted(boolean deleted, Pageable pageable);
}