package com.nextrad.vietphucstore.dtos.responses.api.dashboard;

public record DashboardResponse(
        long totalNewUsers,
        long totalOldUsers,
        long totalPendingOrders,
        long totalAwaitingPickupOrders,
        long totalAwaitingDeliveryOrders,
        long totalInTransitOrders,
        long totalDeliveredOrders,
        long totalCanceledOrders,
        long totalSell,
        double totalRevenueThisWeek,
        double totalRevenueLastWeek
) {

}
