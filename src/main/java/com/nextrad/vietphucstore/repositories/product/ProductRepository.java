package com.nextrad.vietphucstore.repositories.product;

import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            ProductStatus status,
            Collection<String> productTypeNames,
            Collection<String> productCollectionNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            Collection<String> productTypeNames,
            Collection<String> productCollectionNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Optional<Product> findByIdAndStatusNot(UUID id, ProductStatus status);
}