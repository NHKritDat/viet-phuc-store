package com.nextrad.vietphucstore.repositories.user;

import com.nextrad.vietphucstore.entities.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    boolean existsByIdAndAvailable(UUID id, boolean available);

    void deleteByExpAtAfter(Date expAtAfter);
}