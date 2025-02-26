package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.api.order.*;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.api.order.*;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {
    String addToCart(ModifyCartRequest request);

    String removeFromCart(ModifyCartRequest request);

    Page<CartInfo> getCartInfo(PageableRequest request);

    String checkout(CreateOrder request);

    String nextStatus(String orderId);

    String previousStatus(String orderId);

    Page<HistoryOrderProduct> getHistoryOrderProducts(PageableRequest request);

    FeedbackResponse doFeedback(UUID orderDetailId, FeedbackRequest request);

    FeedbackResponse getFeedback(UUID orderDetailId);

    Page<SearchOrder> getOrders(String search, OrderStatus status, PageableRequest request);

    OrderResponse getOrderDetailForStaff(String id);

    String getCheckId();

    Page<CurrentOrderHistory> getCurrentOrderHistory(PageableRequest pageableRequest);

    OrderResponse getCurrentOrderDetailHistory(String id);

    Page<TransactionsResponse> getOrderTransactionsHistory(PageableRequest pageableRequest);

    String createOrderForStaff(PreparedOrder request);

    OrderResponse updateOrderForStaff(UpdateOrder request, String orderId);

    OrderResponse cancelOrderForStaff(String orderId);
}
