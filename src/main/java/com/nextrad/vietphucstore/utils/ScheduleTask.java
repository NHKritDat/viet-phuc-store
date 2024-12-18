package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.services.NotificationService;
import com.nextrad.vietphucstore.services.OrderService;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleTask {
    private final UserService userService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final ProductService productService;
}
