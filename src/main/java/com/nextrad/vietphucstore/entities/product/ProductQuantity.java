package com.nextrad.vietphucstore.entities.product;

import com.nextrad.vietphucstore.configs.UuidToBinaryConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "product_quantities")
public class ProductQuantity {
    @Id
    @Convert(converter = UuidToBinaryConverter.class)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_size_id", nullable = false)
    private ProductSize productSize;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean deleted = false;

}