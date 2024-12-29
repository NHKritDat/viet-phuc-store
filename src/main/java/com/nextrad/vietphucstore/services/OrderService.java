package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.order.CreateOrder;
import com.nextrad.vietphucstore.dtos.requests.order.FeedbackRequest;
import com.nextrad.vietphucstore.dtos.requests.order.ModifyCartRequest;
import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.order.CartInfo;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.order.OrderHistory;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface OrderService {
    String addToCart(ModifyCartRequest request);

    String removeFromCart(ModifyCartRequest request);

    Page<CartInfo> getCartInfo(PageableRequest request);

    String checkout(CreateOrder request);

    String nextStatus(String orderId);

    String previousStatus(String orderId);

    Page<OrderHistory> getHistoryOrders(PageableRequest request);

    Page<OrderHistory> getOrdersForStaff(PageableRequest request);

    FeedbackResponse doFeedback(UUID orderDetailId, FeedbackRequest request);

    FeedbackResponse getFeedback(UUID orderDetailId);

}
