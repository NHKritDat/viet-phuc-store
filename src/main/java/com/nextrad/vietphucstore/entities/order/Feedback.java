package com.nextrad.vietphucstore.entities.order;

import com.nextrad.vietphucstore.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "feedbacks")
@EntityListeners(AuditingEntityListener.class)
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @Column(nullable = false)
    private int rating = 5;

    @Column(nullable = false)
    private String content = "";

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;

    @Column(nullable = false)
    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedDate;

}