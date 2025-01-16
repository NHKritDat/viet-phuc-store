package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, UUID> {
    Optional<Feedback> findByOrderDetail_Id(UUID orderDetailId);

    Page<Feedback> findByOrderDetail_ProductQuantity_Product_IdAndDeleted(UUID orderDetailProductQuantityProductId, boolean deleted, Pageable pageable);

    Page<Feedback> findByOrderDetail_ProductQuantity_Product_Id(UUID orderDetailProductQuantityProductId, Pageable pageable);

    List<Feedback> findByOrderDetail_ProductQuantity_Product_IdAndDeleted(UUID orderDetailProductQuantityProductId, boolean deleted);

    boolean existsByOrderDetail_Id(UUID orderDetailId);

    @Query(
            value = "select avg(f.rating) from Feedback f " +
                    "where f.deleted=false " +
                    "and f.orderDetail.productQuantity.product.id=:productId"
    )
    double avgRatingByProductId(@Param("productId") UUID productId);
}