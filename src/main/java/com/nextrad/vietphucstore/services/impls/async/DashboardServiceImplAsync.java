package com.nextrad.vietphucstore.services.impls.async;

import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountUser;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepo;
import com.nextrad.vietphucstore.repositories.order.OrderRepo;
import com.nextrad.vietphucstore.repositories.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class DashboardServiceImplAsync {
    private final OrderDetailRepo orderDetailRepo;
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    @Async
    public CompletableFuture<CountOrder> countOrdersAsync() {
        return CompletableFuture.supplyAsync(orderRepo::countOrder);
    }

    @Async
    public CompletableFuture<CountUser> countUsersAsync() {
        return CompletableFuture.supplyAsync(userRepo::countUser);
    }

    @Async
    public CompletableFuture<SumRevenue> sumRevenueAsync() {
        return CompletableFuture.supplyAsync(orderRepo::sumRevenue);
    }

    @Async
    public CompletableFuture<Long> totalProductSellAsync() {
        return CompletableFuture.supplyAsync(orderDetailRepo::totalProductSell);
    }

}
