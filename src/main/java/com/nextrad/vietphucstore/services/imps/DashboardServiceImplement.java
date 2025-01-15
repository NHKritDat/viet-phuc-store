package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.responses.api.dashboard.DashboardResponse;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountUser;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue;
import com.nextrad.vietphucstore.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardServiceImplement implements DashboardService {
    private final AsyncDashboardServiceImpl asyncDashboardServiceImpl;

    @Override
    public DashboardResponse getDashboardResponse() {
        CompletableFuture<CountOrder> countOrdersAsync = asyncDashboardServiceImpl.countOrdersAsync();
        CompletableFuture<Long> totalProductSellAsync = asyncDashboardServiceImpl.totalProductSellAsync();
        CompletableFuture<CountUser> countUsersAsync = asyncDashboardServiceImpl.countUsersAsync();
        CompletableFuture<SumRevenue> sumRevenueAsync = asyncDashboardServiceImpl.sumRevenueAsync();
        CompletableFuture.allOf(countOrdersAsync, totalProductSellAsync, countUsersAsync, sumRevenueAsync).join();
        return new DashboardResponse(
                countUsersAsync.join().newUser(), countUsersAsync.join().oldUser(),
                countOrdersAsync.join().pending(),
                countOrdersAsync.join().pickup(),
                countOrdersAsync.join().delivery(),
                countOrdersAsync.join().transit(),
                countOrdersAsync.join().delivered(),
                countOrdersAsync.join().canceled(),
                totalProductSellAsync.join(),
                sumRevenueAsync.join().thisWeek(),
                sumRevenueAsync.join().lastWeek()
        );
    }

}
