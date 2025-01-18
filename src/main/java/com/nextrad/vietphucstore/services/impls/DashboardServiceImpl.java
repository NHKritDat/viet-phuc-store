package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.responses.api.dashboard.DashboardResponse;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountUser;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public DashboardResponse getDashboardResponse() {
        //Start async
        CompletableFuture<CountOrder> countOrdersAsync = CompletableFuture.supplyAsync(orderRepository::countOrder)
                .exceptionally(v -> new CountOrder(0, 0, 0, 0, 0, 0));
        CompletableFuture<Long> totalProductSellAsync = CompletableFuture.supplyAsync(orderDetailRepository::totalProductSell)
                .exceptionally(v -> 0L);
        CompletableFuture<CountUser> countUsersAsync = CompletableFuture.supplyAsync(userRepository::countUser)
                .exceptionally(v -> new CountUser(0, 0));
        CompletableFuture<SumRevenue> sumRevenueAsync = CompletableFuture.supplyAsync(orderRepository::sumRevenue)
                .exceptionally(v -> new SumRevenue(0, 0));

        //Wait to get result from all async
        return CompletableFuture.allOf(countOrdersAsync, totalProductSellAsync, countUsersAsync, sumRevenueAsync)
                .thenApply(v -> {
                    //Get result
                    CountUser countUser = countUsersAsync.join();
                    CountOrder countOrder = countOrdersAsync.join();
                    Long totalProductSold = totalProductSellAsync.join();
                    SumRevenue sumRevenue = sumRevenueAsync.join();

                    //return result
                    return new DashboardResponse(
                            countUser.newUser(), countUser.oldUser(), countOrder.pending(), countOrder.pickup(),
                            countOrder.delivery(), countOrder.transit(), countOrder.delivered(), countOrder.canceled(),
                            totalProductSold, sumRevenue.thisWeek(), sumRevenue.lastWeek()
                    );
                }).join();
    }

}
