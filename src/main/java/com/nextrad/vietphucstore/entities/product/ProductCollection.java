package com.nextrad.vietphucstore.entities.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "product_collections")
public class ProductCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String description;

    @Lob
    private String images;

    @Column(nullable = false)
    private boolean deleted = false;

}