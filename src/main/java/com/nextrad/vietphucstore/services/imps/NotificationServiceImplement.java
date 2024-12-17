package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.repositories.NotificationRepository;
import com.nextrad.vietphucstore.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplement implements NotificationService {
    private final NotificationRepository notificationRepository;
}
