package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.responses.api.dashboard.DashboardResponse;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountOrder;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.CountUser;
import com.nextrad.vietphucstore.dtos.responses.inner.dashboard.SumRevenue;
import com.nextrad.vietphucstore.services.DashboardService;
import com.nextrad.vietphucstore.services.impls.async.DashboardServiceImplAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardServiceImplAsync dashboardServiceImplAsync;

    @Override
    public DashboardResponse getDashboardResponse() {
        //Start async
        CompletableFuture<CountOrder> countOrdersAsync = dashboardServiceImplAsync.countOrdersAsync();
        CompletableFuture<Long> totalProductSellAsync = dashboardServiceImplAsync.totalProductSellAsync();
        CompletableFuture<CountUser> countUsersAsync = dashboardServiceImplAsync.countUsersAsync();
        CompletableFuture<SumRevenue> sumRevenueAsync = dashboardServiceImplAsync.sumRevenueAsync();

        //Wait to get result from all async
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
    }

}
