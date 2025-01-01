package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.order.CreateOrder;
import com.nextrad.vietphucstore.dtos.requests.order.FeedbackRequest;
import com.nextrad.vietphucstore.dtos.requests.order.ModifyCartRequest;
import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.order.CartInfo;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.order.OrderHistory;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiListItemResponse;
import com.nextrad.vietphucstore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/carts")
    public ResponseEntity<ApiListItemResponse<CartInfo>> getCartInfo(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<CartInfo> response = orderService.getCartInfo(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                null
        ));
    }

    @PostMapping("/carts")
    public ResponseEntity<ApiItemResponse<Object>> addToCart(@RequestBody ModifyCartRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, orderService.addToCart(request)));
    }

    @DeleteMapping("/carts")
    public ResponseEntity<ApiItemResponse<Object>> removeFromCart(@RequestBody ModifyCartRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, orderService.removeFromCart(request)));
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiItemResponse<Object>> checkout(@RequestBody CreateOrder request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, orderService.checkout(request)));
    }

    @PutMapping("/{id}/next-status/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<Object>> nextStatus(@PathVariable String id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, orderService.nextStatus(id)));
    }

    @PutMapping("/{id}/previous-status/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<Object>> previousStatus(@PathVariable String id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, orderService.previousStatus(id)));
    }

    @GetMapping("/order-details/current")
    public ResponseEntity<ApiListItemResponse<OrderHistory>> getHistoryOrders(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<OrderHistory> response = orderService.getHistoryOrders(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                null
        ));
    }

    @PostMapping("/order-details/{id}/feedbacks")
    public ResponseEntity<ApiItemResponse<FeedbackResponse>> doFeedback(
            @PathVariable UUID id,
            @RequestBody FeedbackRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(orderService.doFeedback(id, request), null));
    }

    @GetMapping("/order-details/{id}/feedbacks")
    public ResponseEntity<ApiItemResponse<FeedbackResponse>> getFeedback(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(orderService.getFeedback(id), null));
    }
}

