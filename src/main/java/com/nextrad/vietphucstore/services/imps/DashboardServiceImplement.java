package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.responses.dashboard.DashboardResponse;
import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImplement implements DashboardService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public DashboardResponse getDashboardResponse() {
        long totalNewUsers = userRepository.countNewUsers();
        long totalOldUsers = userRepository.countOldUsers();
        List<Order> orders = orderRepository.findAll();
        long totalPendingOrders = orders.stream().filter(o -> o.getStatus() == OrderStatus.PENDING).count();
        long totalAwaitingPickupOrders = orders.stream().filter(o -> o.getStatus() == OrderStatus.AWAITING_PICKUP).count();
        long totalAwaitingDeliveryOrders = orders.stream().filter(o -> o.getStatus() == OrderStatus.AWAITING_DELIVERY).count();
        long totalInTransitOrders = orders.stream().filter(o -> o.getStatus() == OrderStatus.IN_TRANSIT).count();
        long totalDeliveredOrders = orders.stream().filter(o -> o.getStatus() == OrderStatus.DELIVERED).count();
        long totalCanceledOrders = orders.stream().filter(o -> o.getStatus() == OrderStatus.CANCELED).count();
        long totalSell = orderDetailRepository.findAll().stream()
                .map(od -> od.getProductQuantity().getProduct()).distinct().count();
        double totalRevenueThisWeek = orderDetailRepository.sumRevenueThisWeek();
        double totalRevenueLastWeek = orderDetailRepository.sumRevenueLastWeek();
        return new DashboardResponse(
                totalNewUsers, totalOldUsers, totalPendingOrders, totalAwaitingPickupOrders,
                totalAwaitingDeliveryOrders, totalInTransitOrders, totalDeliveredOrders, totalCanceledOrders,
                totalSell, totalRevenueThisWeek, totalRevenueLastWeek
        );
    }
}
