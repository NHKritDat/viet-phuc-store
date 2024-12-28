package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.order.CreateOrder;
import com.nextrad.vietphucstore.dtos.requests.order.ModifyCartRequest;
import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.order.CartInfo;
import org.springframework.data.domain.Page;

public interface OrderService {
    String addToCart(ModifyCartRequest request);

    String removeFromCart(ModifyCartRequest request);

    Page<CartInfo> getCartInfo(PageableRequest request);

    String checkout(CreateOrder request);

    String nextStatus(String orderId);

    String previousStatus(String orderId);


}
