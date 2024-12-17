package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.repositories.CartRepository;
import com.nextrad.vietphucstore.repositories.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.OrderRepository;
import com.nextrad.vietphucstore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImplement implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final FeedbackRepository feedbackRepository;
}
