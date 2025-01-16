package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.requests.api.order.*;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.api.order.*;
import com.nextrad.vietphucstore.entities.order.Cart;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.CartRepo;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepo;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepo;
import com.nextrad.vietphucstore.repositories.order.OrderRepo;
import com.nextrad.vietphucstore.repositories.product.ProductQuantityRepo;
import com.nextrad.vietphucstore.repositories.user.UserRepo;
import com.nextrad.vietphucstore.services.OrderService;
import com.nextrad.vietphucstore.services.impls.async.OrderServiceImplAsync;
import com.nextrad.vietphucstore.utils.EmailUtil;
import com.nextrad.vietphucstore.utils.IdUtil;
import com.nextrad.vietphucstore.utils.ObjectMapperUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final CartRepo cartRepo;
    private final FeedbackRepo feedbackRepo;
    private final UserRepo userRepo;
    private final ProductQuantityRepo productQuantityRepo;
    private final OrderServiceImplAsync orderServiceImplAsync;
    private final PageableUtil pageableUtil;
    private final IdUtil idUtil;
    private final ObjectMapperUtil objectMapperUtil;
    private final EmailUtil emailUtil;

    @Override
    public String addToCart(ModifyCartRequest request) {
        Optional<Cart> cart = cartRepo.findByProductQuantity_Id(request.productQuantityId());
        if (cart.isPresent()) {
            cart.get().setQuantity(
                    Math.min(cart.get().getQuantity() + request.quantity(),
                            cart.get().getProductQuantity().getQuantity())
            );
            cartRepo.save(cart.get());
        } else {
            Cart newCart = new Cart();
            newCart.setUser(userRepo.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
            ProductQuantity productQuantity = productQuantityRepo.findByIdAndDeleted(
                    request.productQuantityId(),
                    false
            ).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_FOUND));
            newCart.setProductQuantity(productQuantity);
            newCart.setQuantity(Math.min(request.quantity(), productQuantity.getQuantity()));
            cartRepo.save(newCart);
        }
        return "Bạn đã thêm vào giỏ hàng thành công";
    }

    @Override
    public String removeFromCart(ModifyCartRequest request) {
        Optional<Cart> cart = cartRepo.findByProductQuantity_Id(request.productQuantityId());
        if (cart.isEmpty())
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        else {
            cart.get().setQuantity(
                    Math.max(cart.get().getQuantity() - request.quantity(), 0)
            );
            if (cart.get().getQuantity() == 0)
                cartRepo.delete(cart.get());
            else
                cartRepo.save(cart.get());
        }
        return "Bạn đã bớt khỏi giỏ hàng thành công";
    }

    @Override
    public Page<CartInfo> getCartInfo(PageableRequest request) {
        Page<Cart> carts = cartRepo.findByUser_Email(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                pageableUtil.getPageable(Cart.class, request)
        );
        return carts.map(objectMapperUtil::mapCartInfo);
    }

    @Override
    public String checkout(CreateOrder request) {
        Order order = new Order();
        order.setUser(userRepo.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        order.setEmail(request.email());
        order.setName(request.name());
        order.setProvince(request.province());
        order.setDistrict(request.district());
        order.setAddress(request.address());
        order.setPhone(request.phone());
        List<Cart> carts = cartRepo.findByUser_Email(getCurrentUserEmail());
        if (carts.isEmpty())
            throw new AppException(ErrorCode.CART_EMPTY);
        double productTotal = carts.stream().mapToDouble(
                cart -> {
                    if (cart.getProductQuantity().getQuantity() < cart.getQuantity())
                        throw new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
                    return cart.getProductQuantity().getProduct().getUnitPrice() * cart.getQuantity();
                }
        ).sum();
        order.setProductTotal(productTotal);
        order.setShippingFee(request.shippingFee());
        order.setShippingMethod(request.shippingMethod());
        order.setPaymentMethod(request.paymentMethod());
        order.setId(idUtil.genId(productTotal + request.shippingFee(), new Date()));
        orderRepo.save(order);

        carts.forEach(cart -> {
            ProductQuantity productQuantity = cart.getProductQuantity();
            productQuantity.setQuantity(productQuantity.getQuantity() - cart.getQuantity());
            productQuantityRepo.save(productQuantity);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setQuantity(cart.getQuantity());
            orderDetailRepo.save(orderDetail);
            cartRepo.delete(cart);
        });

        emailUtil.orderDetail(orderRepo.findById(order.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));

        return "Bạn đã thực hiện đơn hàng thành công. Vui lòng kiểm tra email để xem chi tiết đơn hàng";
    }

    @Override
    public String nextStatus(String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.PENDING)
            order.setStatus(OrderStatus.AWAITING_PICKUP);
        else if (order.getStatus() == OrderStatus.AWAITING_PICKUP)
            order.setStatus(OrderStatus.AWAITING_DELIVERY);
        else if (order.getStatus() == OrderStatus.AWAITING_DELIVERY)
            order.setStatus(OrderStatus.IN_TRANSIT);
        else if (order.getStatus() == OrderStatus.IN_TRANSIT)
            order.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);
        return "Trạng thái của đơn hàng đã được cập nhật";
    }

    @Override
    public String previousStatus(String orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.IN_TRANSIT)
            order.setStatus(OrderStatus.AWAITING_DELIVERY);
        else if (order.getStatus() == OrderStatus.AWAITING_DELIVERY)
            order.setStatus(OrderStatus.AWAITING_PICKUP);
        else if (order.getStatus() == OrderStatus.AWAITING_PICKUP)
            order.setStatus(OrderStatus.PENDING);
        orderRepo.save(order);
        return "Trạng thái của đơn hàng đã được đảo ngược";
    }

    @Override
    public Page<OrderHistory> getHistoryOrders(PageableRequest request) {
        Page<OrderDetail> orderDetails = orderDetailRepo
                .findByOrder_User_EmailAndOrder_Status(
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        OrderStatus.DELIVERED,
                        pageableUtil.getPageable(OrderDetail.class, request)
                );
        return orderDetails.map(od ->
                objectMapperUtil.mapOrderHistory(od, feedbackRepo.existsByOrderDetail_Id(od.getId()))
        );
    }

    @Override
    public FeedbackResponse doFeedback(UUID orderDetailId, FeedbackRequest request) {
        //Get order detail
        OrderDetail orderDetail = orderDetailRepo.findById(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

        //Check correct user order the product
        if (!orderDetail.getOrder().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            throw new AppException(ErrorCode.NO_PERMISSION);

        //Save feedback
        Feedback feedback = feedbackRepo.save(objectMapperUtil.mapFeedback(request, new Feedback(), orderDetail));

        //Update avg rating of product async
        Product product = orderDetail.getProductQuantity().getProduct();
        orderServiceImplAsync.avgRating(product.getId())
                .thenComposeAsync(rating ->
                        orderServiceImplAsync.assignRating(product, rating)
                );

        return objectMapperUtil.mapFeedbackResponse(feedback);
    }

    @Override
    public FeedbackResponse getFeedback(UUID orderDetailId) {
        return objectMapperUtil.mapFeedbackResponse(
                feedbackRepo.findByOrderDetail_Id(orderDetailId)
                        .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND))
        );
    }

    @Override
    public Page<SearchOrder> getOrders(String search, PageableRequest request) {
        return orderRepo.findByEmailContainingIgnoreCase(
                search,
                pageableUtil.getPageable(Order.class, request)
        ).map(objectMapperUtil::mapSearchOrder);
    }

    @Override
    public OrderResponse getOrderDetailForStaff(String id) {
        return orderRepo.findById(id)
                .map(objectMapperUtil::mapOrderResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public String getCheckId() {
        return idUtil.genCheckId();
    }

    @Override
    public Page<CurrentOrderHistory> getCurrentOrderHistory(PageableRequest pageableRequest) {
        Page<Order> orders = orderRepo.findByUser_Email(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                pageableUtil.getPageable(Order.class, pageableRequest)
        );
        return orders.map(objectMapperUtil::mapCurrentOrderHistory);
    }

    @Override
    public OrderResponse getCurrentOrderDetailHistory(String id) {
        return orderRepo.findByIdAndUser_Email(
                        id,
                        SecurityContextHolder.getContext().getAuthentication().getName()
                ).map(objectMapperUtil::mapOrderResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public Page<TransactionsResponse> getOrderTransactionsHistory(PageableRequest pageableRequest) {
        return orderRepo.findByUser_Email(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                pageableUtil.getPageable(Order.class, pageableRequest)
        ).map(objectMapperUtil::mapTransactionsResponse);
    }

    @Override
    public String createOrderForStaff(PreparedOrder request) {
        //tạo order
        Order order = new Order();

        order.setUser(userRepo.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        order.setEmail(request.email());
        order.setName(request.name());
        order.setProvince(request.province());
        order.setDistrict(request.district());
        order.setAddress(request.address());
        order.setPhone(request.phone());

        List<SelectedProductRequest> products = request.details();
        if (products.isEmpty())
            throw new AppException(ErrorCode.MISSING_SELECT_PRODUCT);

        double productTotal = products.stream().mapToDouble(
                product -> {
                    if (findProductQuantity(product.productQuantityId()).getQuantity() < product.quantity())
                        throw new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
                    return findProductQuantity(product.productQuantityId()).getProduct().getUnitPrice() * product.quantity();
                }
        ).sum();

        order.setProductTotal(productTotal);
        order.setShippingFee(request.shippingFee());
        order.setShippingMethod(request.shippingMethod());
        order.setPaymentMethod(request.paymentMethod());
        order.setId(idUtil.genId(productTotal + request.shippingFee(), new Date()));
        orderRepo.save(order);

        products.forEach(product -> {
            ProductQuantity productQuantity = findProductQuantity(product.productQuantityId());
            productQuantity.setQuantity(productQuantity.getQuantity() - product.quantity());
            productQuantityRepo.save(productQuantity);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setQuantity(product.quantity());
            orderDetailRepo.save(orderDetail);
        });

        emailUtil.orderDetail(orderRepo.findById(order.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));

        return "Bạn đã thực hiện thành công đơn hàng";
    }

    @Override
    public OrderResponse updateOrderForStaff(UpdateOrder request, String orderId) {
        Order existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        existingOrder.setEmail(request.email());
        existingOrder.setName(request.name());
        existingOrder.setPhone(request.phone());
        existingOrder.setId(orderId);
        orderRepo.save(existingOrder);
        return objectMapperUtil.mapOrderResponse(existingOrder);
    }

    @Override
    public OrderResponse cancelOrderForStaff(String orderId) {
        Order existingOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (existingOrder.getStatus() == OrderStatus.PENDING) {
            existingOrder.setStatus(OrderStatus.CANCELED);
            orderRepo.save(existingOrder);
            return objectMapperUtil.mapOrderResponse(existingOrder);
        } else if (existingOrder.getStatus() == OrderStatus.CANCELED)
            throw new AppException(ErrorCode.ALREADY_CANCELED);
        else
            throw new AppException(ErrorCode.CAN_NOT_CANCEL);
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private ProductQuantity findProductQuantity(UUID productQuantityId) {
        return productQuantityRepo.findByIdAndDeleted(productQuantityId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_FOUND));
    }
}

