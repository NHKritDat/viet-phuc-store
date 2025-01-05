package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Order> findByUser_Email(String userEmail, Pageable pageable);

    Optional<Order> findByIdAndUser_Email(String id, String userEmail);
}