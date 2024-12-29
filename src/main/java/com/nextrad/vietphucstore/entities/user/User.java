package com.nextrad.vietphucstore.entities.user;

import com.nextrad.vietphucstore.enums.user.UserGender;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
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
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private Date dob = new Date();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGender gender = UserGender.OTHER;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password = "$2a$12$OqwaI0/yLjyf/kkLLd3veelcs7M2ememzdyZiO1jgJPRlIz1mQYpy";

    @Column(nullable = false)
    private String province = "";

    @Column(nullable = false)
    private String district = "";

    @Column(nullable = false)
    private String address = "";

    @Column(nullable = false)
    private String phone = "";

    private String avatar = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.UNVERIFIED;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    @LastModifiedBy
    @Column(nullable = false)
    private String updatedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedDate;

}