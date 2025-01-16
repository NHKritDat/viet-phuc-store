package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue;
import com.nextrad.vietphucstore.entities.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Order> findByUser_Email(String userEmail, Pageable pageable);

    Optional<Order> findByIdAndUser_Email(String id, String userEmail);

    @Query(
            value = "select new com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue(" +
                    "sum(case when function('WEEK',createdDate) = function('WEEK',current date) then productTotal else 0 end)," +
                    "sum(case when function('WEEK',createdDate) < function('WEEK',current date)  then productTotal else 0 end)) " +
                    "from Order " +
                    "where status != 'CANCELED' " +
                    "and (status = 'DELIVERED' or paymentMethod = 'QR')"
    )
    SumRevenue sumRevenue();

    @Query(
            value = "select new com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder(" +
                    "sum(case when status='PENDING' then 1 else 0 end)," +
                    "sum(case when status='AWAITING_PICKUP' then 1 else 0 end)," +
                    "sum(case when status='AWAITING_DELIVERY' then 1 else 0 end)," +
                    "sum(case when status='IN_TRANSIT' then 1 else 0 end)," +
                    "sum(case when status='DELIVERED' then 1 else 0 end)," +
                    "sum(case when status='CANCELED' then 1 else 0 end)) " +
                    "from Order"
    )
    CountOrder countOrder();
}