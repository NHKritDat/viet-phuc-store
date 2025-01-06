package com.nextrad.vietphucstore.entities.product;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_size_id", nullable = false)
    private ProductSize productSize;

    @Column(nullable = false)
    private int quantity;

    private boolean deleted;

}