package com.nextrad.vietphucstore.repositories.product;

import com.nextrad.vietphucstore.entities.product.ProductCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollection, UUID> {
}