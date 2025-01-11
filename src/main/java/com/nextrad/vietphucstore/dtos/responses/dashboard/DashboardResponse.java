package com.nextrad.vietphucstore.dtos.responses.dashboard;

public record DashboardResponse(
    long totalNewUsers,
    long totalOldUsers,
    long totalPendingOrders,
    long totalAwaitingPickupOrders,
    long totalAwaitingDeliveryOrders,
    long totalInTransitOrders,
    long totalDeliveredOrders,
    long totalSell,
    long totalRevenueThisWeek,
    long totalRevenueLastWeek
) {

}
