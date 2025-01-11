package com.nextrad.vietphucstore.entities.order;

import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_quantity_id", nullable = false)
    private ProductQuantity productQuantity;

    private int quantity;

}