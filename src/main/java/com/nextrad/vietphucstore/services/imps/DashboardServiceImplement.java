package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.responses.api.dashboard.DashboardResponse;
import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.DashboardService;
import com.nextrad.vietphucstore.utils.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImplement implements DashboardService {
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public DashboardResponse getDashboardResponse() {
        List<Order> orders = orderRepository.findAll();
        return objectMapperUtil.mapDashboardResponse(
                userRepository.countNewUsers(), userRepository.countOldUsers(),
                orders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count(),
                orders.stream().filter(o -> o.getStatus() == OrderStatus.AWAITING_PICKUP).count(),
                orders.stream().filter(o -> o.getStatus() == OrderStatus.AWAITING_DELIVERY).count(),
                orders.stream().filter(o -> o.getStatus() == OrderStatus.IN_TRANSIT).count(),
                orders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count(),
                orders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELED).count(),
                orderDetailRepository.findAll().stream()
                        .map(od -> od.getProductQuantity().getProduct()).distinct().count(),
                orderDetailRepository.sumRevenueThisWeek(),
                orderDetailRepository.sumRevenueLastWeek()
        );
    }
}
