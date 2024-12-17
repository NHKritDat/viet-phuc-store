package com.nextrad.vietphucstore.services;

import java.util.UUID;

public interface UserService {
    boolean existsByIdAndStatus(UUID id, boolean status);
}
