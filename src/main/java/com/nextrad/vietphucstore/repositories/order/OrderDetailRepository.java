package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    Page<OrderDetail> findByOrder_User_EmailAndOrder_Status(String orderUserEmail, OrderStatus orderStatus, Pageable pageable);
}