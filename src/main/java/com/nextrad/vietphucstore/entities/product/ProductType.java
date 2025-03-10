package com.nextrad.vietphucstore.entities.product;

import com.nextrad.vietphucstore.configs.UuidToBinaryConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "product_types")
public class ProductType {
    @Id
    @Convert(converter = UuidToBinaryConverter.class)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean deleted = false;

}