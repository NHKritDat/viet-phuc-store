package com.nextrad.vietphucstore.repositories;

import com.nextrad.vietphucstore.entities.Token;
import com.nextrad.vietphucstore.enums.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
  boolean existsByIdAndStatus(UUID id, TokenStatus status);
}