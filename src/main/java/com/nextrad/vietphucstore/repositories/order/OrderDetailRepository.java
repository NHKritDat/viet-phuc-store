package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.dtos.requests.inner.product.TopProductRequest;
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
            value = "select new com.nextrad.vietphucstore.dtos.requests.inner.product" +
                    ".TopProductRequest(p.id, p.name, p.unitPrice, p.pictures, sum(od.quantity), p.avgRating)  " +
                    "from OrderDetail od " +
                    "left join ProductQuantity pq on od.productQuantity.id = pq.id " +
                    "left join Product p on pq.product.id = p.id " +
                    "where p.status != 'DELETED' " +
                    "group by p.id " +
                    "order by sum(od.quantity) desc " +
                    "limit :size"
    )
    List<TopProductRequest> findTopProduct(@Param("size") int size);

    @Query(
            value = "select nullif(sum(od.quantity),0) from OrderDetail od " +
                    "where od.order.status != 'CANCELED'"
    )
    long totalProductSell();
}