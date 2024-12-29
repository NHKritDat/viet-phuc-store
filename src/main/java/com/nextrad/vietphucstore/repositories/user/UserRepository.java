package com.nextrad.vietphucstore.repositories.user;

import com.nextrad.vietphucstore.dtos.responses.user.UserDetail;
import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmailAndStatusNotLike(String email, UserStatus status);

    Page<UserDetail> findByRoleNotLikeAndNameContainsIgnoreCase(UserRole role, String name, Pageable pageable);

    Optional<User> findByIdAndRoleNotLike(UUID id, UserRole role);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);
}