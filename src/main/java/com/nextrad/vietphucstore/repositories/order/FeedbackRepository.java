package com.nextrad.vietphucstore.repositories.order;

import com.nextrad.vietphucstore.entities.order.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
}