package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountUser;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardServiceImplAsync {
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Async
    public CompletableFuture<CountOrder> countOrdersAsync() {
        return CompletableFuture.supplyAsync(orderRepository::countOrder);
    }

    @Async
    public CompletableFuture<CountUser> countUsersAsync() {
        return CompletableFuture.supplyAsync(userRepository::countUser);
    }

    @Async
    public CompletableFuture<SumRevenue> sumRevenueAsync() {
        return CompletableFuture.supplyAsync(orderRepository::sumRevenue);
    }

    @Async
    public CompletableFuture<Long> totalProductSellAsync() {
        return CompletableFuture.supplyAsync(orderDetailRepository::totalProductSell);
    }

}
