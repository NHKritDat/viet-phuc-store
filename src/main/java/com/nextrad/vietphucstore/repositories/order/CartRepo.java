package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepo extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByProductQuantity_Id(UUID productQuantityId);

    Page<Cart> findByUser_Email(String userEmail, Pageable pageable);

    List<Cart> findByUser_Email(String userEmail);
}