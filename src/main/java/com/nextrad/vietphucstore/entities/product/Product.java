package com.nextrad.vietphucstore.entities.product;

import com.nextrad.vietphucstore.enums.product.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private double unitPrice;

    @Lob
    private String pictures;

    private int weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.IN_STOCK;

    @ManyToOne
    @JoinColumn(name = "product_collection_id")
    private ProductCollection productCollection;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", orphanRemoval = true)
    private Set<ProductQuantity> productQuantities = new LinkedHashSet<>();

    private double avgRating = 0.0;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedDate;
}