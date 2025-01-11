package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    Page<OrderDetail> findByOrder_User_EmailAndOrder_Status(String orderUserEmail, OrderStatus orderStatus, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "select sum(product_total) as total from orders " +
                    "where week(created_date) = week(current_date) " +
                    "and year(created_date) = year(current_date) " +
                    "and (status = 'DELIVERED' or payment_method = 'QR')"
    )
    long sumRevenueThisWeek();

    @Query(
            nativeQuery = true,
            value = "select sum(product_total) as total from orders " +
                    "where week(created_date) < week(current_date) " +
                    "and year(created_date) = year(current_date) " +
                    "and (status = 'DELIVERED' or payment_method = 'QR')"
    )
    long sumRevenueLastWeek();
}