package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.repositories.notification.NotificationRepository;
import com.nextrad.vietphucstore.services.NotificationService;
import com.nextrad.vietphucstore.utils.EmailUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplement implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailUtil emailUtil;
    private final PageableUtil pageableUtil;
}
