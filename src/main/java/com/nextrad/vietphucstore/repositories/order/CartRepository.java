package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
}