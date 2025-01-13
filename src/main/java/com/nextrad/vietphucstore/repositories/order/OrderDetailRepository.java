package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.dtos.requests.product.TopProductRequest;
import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    Page<OrderDetail> findByOrder_User_EmailAndOrder_Status(String orderUserEmail, OrderStatus orderStatus, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "select ifnull(sum(product_total), 0) as total from orders " +
                    "where week(created_date) = week(current_date) " +
                    "and year(created_date) = year(current_date) " +
                    "and status != 'CANCELED' " +
                    "and (status = 'DELIVERED' or payment_method = 'QR')"
    )
    double sumRevenueThisWeek();

    @Query(
            nativeQuery = true,
            value = "select ifnull(sum(product_total), 0) as total from orders " +
                    "where week(created_date) < week(current_date) " +
                    "and year(created_date) = year(current_date) " +
                    "and status != 'CANCELED' " +
                    "and (status = 'DELIVERED' or payment_method = 'QR')"
    )
    double sumRevenueLastWeek();

    @Query(
            value = "select new com.nextrad.vietphucstore.dtos.requests.product" +
                    ".TopProductRequest(p.id, p.name, p.unitPrice, p.pictures, sum(od.quantity))  " +
                    "from OrderDetail od " +
                    "left join ProductQuantity pq on od.productQuantity.id = pq.id " +
                    "left join Product p on pq.product.id = p.id " +
                    "group by p.id " +
                    "order by sum(od.quantity) desc " +
                    "limit :size"
    )
    List<TopProductRequest> findTopProduct(@Param("size") int size);
}