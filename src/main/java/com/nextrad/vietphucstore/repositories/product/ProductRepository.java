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
    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productTypeNames,
            Collection<String> productCollectionNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productTypeNames,
            Collection<String> productCollectionNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Optional<Product> findByIdAndStatusNot(UUID id, ProductStatus status);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productTypeNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetween(
            String name,
            double minPrice,
            double maxPrice,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productCollectionNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productTypeNames,
            Collection<String> productCollectionNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productTypeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            Collection<String> productCollectionNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productTypeNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productCollectionNames,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductCollection_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productTypeNames,
            Collection<String> productCollectionNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductQuantities_ProductSize_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productQuantitiesProductSizeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productTypeNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameIn(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Collection<String> productCollectionNames,
            Pageable pageable);

    Page<Product> findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNot(
            String name,
            double minPrice,
            double maxPrice,
            ProductStatus status,
            Pageable pageable);

    Optional<Product> findByIdAndStatus(UUID id, ProductStatus status);

    Page<Product> findByStatusNot(ProductStatus status, Pageable pageable);
}