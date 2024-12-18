package com.nextrad.vietphucstore.entities.product;

import com.nextrad.vietphucstore.enums.product.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private int quantity;

    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.IN_STOCK;

    private String sizes;

    @ManyToOne
    @JoinColumn(name = "product_collection_id")
    private ProductCollection productCollection;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

}