package com.nextrad.vietphucstore.repositories.user;

import com.nextrad.vietphucstore.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByFullName(String fullName);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}