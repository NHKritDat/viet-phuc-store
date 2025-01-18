package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.requests.api.order.*;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.api.order.*;
import com.nextrad.vietphucstore.dtos.responses.inner.order.SelectedProduct;
import com.nextrad.vietphucstore.entities.order.Cart;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.enums.order.PaymentMethod;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.CartRepository;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.product.ProductQuantityRepository;
import com.nextrad.vietphucstore.repositories.product.ProductRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.OrderService;
import com.nextrad.vietphucstore.utils.EmailUtil;
import com.nextrad.vietphucstore.utils.IdUtil;
import com.nextrad.vietphucstore.utils.ObjectMapperUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductQuantityRepository productQuantityRepository;
    private final PageableUtil pageableUtil;
    private final IdUtil idUtil;
    private final ObjectMapperUtil objectMapperUtil;
    private final EmailUtil emailUtil;

    @Override
    public String addToCart(ModifyCartRequest request) {
        String email = getCurrentUserEmail();
        Cart cart = cartRepository.findByProductQuantity_IdAndUser_Email(request.productQuantityId(), email)
                .orElse(new Cart());
        if (cart.getId() != null) {
            cart.setQuantity(Math.min(
                    cart.getQuantity() + request.quantity(),
                    cart.getProductQuantity().getQuantity()
            ));
        } else {
            cart.setUser(userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
            ProductQuantity productQuantity = productQuantityRepository.findByIdAndDeleted(
                    request.productQuantityId(), false
            ).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_FOUND));
            cart.setProductQuantity(productQuantity);
            cart.setQuantity(Math.min(request.quantity(), productQuantity.getQuantity()));
        }
        cartRepository.save(cart);
        return "Bạn đã thêm vào giỏ hàng thành công";
    }

    @Override
    public String removeFromCart(ModifyCartRequest request) {
        String email = getCurrentUserEmail();
        Cart cart = cartRepository.findByProductQuantity_IdAndUser_Email(request.productQuantityId(), email)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        cart.setQuantity(Math.max(cart.getQuantity() - request.quantity(), 0));
        if (cart.getQuantity() == 0)
            cartRepository.delete(cart);
        else
            cartRepository.save(cart);

        return "Bạn đã bớt khỏi giỏ hàng thành công";
    }

    @Override
    public Page<CartInfo> getCartInfo(PageableRequest request) {
        Page<Cart> carts = cartRepository.findByUser_Email(
                getCurrentUserEmail(),
                pageableUtil.getPageable(Cart.class, request)
        );
        return carts.map(objectMapperUtil::mapCartInfo);
    }

    @Override
    @Transactional
    public String checkout(CreateOrder request) {
        String email = getCurrentUserEmail();
        Order order = new Order();
        order.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        order.setEmail(request.email());
        order.setName(request.name());
        order.setProvince(request.province());
        order.setDistrict(request.district());
        order.setAddress(request.address());
        order.setPhone(request.phone());
        List<Cart> carts = cartRepository.findByUser_Email(email);
        if (carts.isEmpty())
            throw new AppException(ErrorCode.CART_EMPTY);
        double productTotal = carts.stream().mapToDouble(
                cart -> {
                    if (cart.getProductQuantity().getQuantity() < cart.getQuantity())
                        throw new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
                    return cart.getProductQuantity().getProduct().getUnitPrice() * cart.getQuantity();
                }
        ).sum();
        saveOrder(order, productTotal, request.shippingFee(), request.shippingMethod(), request.paymentMethod());

        List<OrderDetail> orderDetails = new ArrayList<>();
        carts.forEach(cart -> {
            ProductQuantity productQuantity = cart.getProductQuantity();
            productQuantity.setQuantity(productQuantity.getQuantity() - cart.getQuantity());
            productQuantityRepository.save(productQuantity);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setQuantity(cart.getQuantity());
            orderDetails.add(orderDetail);
        });
        orderDetailRepository.saveAll(orderDetails);
        cartRepository.deleteAll(carts);

        emailUtil.orderDetail(orderRepository.findById(order.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));

        return "Bạn đã thực hiện đơn hàng thành công. Vui lòng kiểm tra email để xem chi tiết đơn hàng";
    }

    @Override
    public String nextStatus(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.PENDING)
            order.setStatus(OrderStatus.AWAITING_PICKUP);
        else if (order.getStatus() == OrderStatus.AWAITING_PICKUP)
            order.setStatus(OrderStatus.AWAITING_DELIVERY);
        else if (order.getStatus() == OrderStatus.AWAITING_DELIVERY)
            order.setStatus(OrderStatus.IN_TRANSIT);
        else if (order.getStatus() == OrderStatus.IN_TRANSIT)
            order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        return "Trạng thái của đơn hàng đã được cập nhật";
    }

    @Override
    public String previousStatus(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.IN_TRANSIT)
            order.setStatus(OrderStatus.AWAITING_DELIVERY);
        else if (order.getStatus() == OrderStatus.AWAITING_DELIVERY)
            order.setStatus(OrderStatus.AWAITING_PICKUP);
        else if (order.getStatus() == OrderStatus.AWAITING_PICKUP)
            order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        return "Trạng thái của đơn hàng đã được đảo ngược";
    }

    @Override
    public Page<HistoryOrderProduct> getHistoryOrderProducts(PageableRequest request) {
        return orderDetailRepository.findByOrder_User_EmailAndOrder_Status(
                getCurrentUserEmail(), OrderStatus.DELIVERED, pageableUtil.getPageable(OrderDetail.class, request)
        ).map(objectMapperUtil::mapHistoryOrderHistory);
    }

    @Override
    public FeedbackResponse doFeedback(UUID orderDetailId, FeedbackRequest request) {
        //Get order detail
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

        //Check correct user order the product
        if (!orderDetail.getOrder().getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            throw new AppException(ErrorCode.NO_PERMISSION);

        //Save feedback
        Feedback feedback = feedbackRepository.save(objectMapperUtil.mapFeedback(request, new Feedback(), orderDetail));

        //Update avg rating of product async
        Product product = orderDetail.getProductQuantity().getProduct();
        CompletableFuture.supplyAsync(() -> feedbackRepository.avgRatingByProductId(product.getId()))
                .thenAcceptAsync(rating -> {
                    product.setAvgRating(rating);
                    productRepository.save(product);
                });

        return objectMapperUtil.mapFeedbackResponse(feedback);
    }

    @Override
    public FeedbackResponse getFeedback(UUID orderDetailId) {
        return objectMapperUtil.mapFeedbackResponse(feedbackRepository.findByOrderDetail_Id(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND)));
    }

    @Override
    public Page<SearchOrder> getOrders(String search, PageableRequest request) {
        return orderRepository.findByEmailContainingIgnoreCase(
                search, pageableUtil.getPageable(Order.class, request)
        ).map(objectMapperUtil::mapSearchOrder);
    }

    @Override
    public OrderResponse getOrderDetailForStaff(String id) {
        return objectMapperUtil.mapOrderResponse(orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));
    }

    @Override
    public String getCheckId() {
        return idUtil.genCheckId();
    }

    @Override
    public Page<CurrentOrderHistory> getCurrentOrderHistory(PageableRequest pageableRequest) {
        return orderRepository.findByUser_Email(
                getCurrentUserEmail(), pageableUtil.getPageable(Order.class, pageableRequest)
        ).map(objectMapperUtil::mapCurrentOrderHistory);
    }

    @Override
    public OrderResponse getCurrentOrderDetailHistory(String id) {
        return objectMapperUtil.mapOrderResponse(orderRepository.findByIdAndUser_Email(id, getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));
    }

    @Override
    public Page<TransactionsResponse> getOrderTransactionsHistory(PageableRequest pageableRequest) {
        return orderRepository.findByUser_Email(
                getCurrentUserEmail(), pageableUtil.getPageable(Order.class, pageableRequest)
        ).map(objectMapperUtil::mapTransactionsResponse);
    }

    @Override
    @Transactional
    public String createOrderForStaff(PreparedOrder request) {
        //tạo order
        Order order = new Order();

        order.setUser(userRepository.findByEmail(getCurrentUserEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        order.setEmail(request.email());
        order.setName(request.name());
        order.setProvince(request.province());
        order.setDistrict(request.district());
        order.setAddress(request.address());
        order.setPhone(request.phone());

        if (request.details().isEmpty())
            throw new AppException(ErrorCode.MISSING_SELECT_PRODUCT);

        List<SelectedProduct> selectedProducts = new ArrayList<>();
        request.details().forEach(detail -> selectedProducts.add(new SelectedProduct(
                findProductQuantity(detail.productQuantityId()),
                detail.quantity()
        )));

        double productTotal = selectedProducts.stream().mapToDouble(
                product -> {
                    if (product.productQuantity().getQuantity() < product.quantity())
                        throw new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH);
                    return product.productQuantity().getProduct().getUnitPrice() * product.quantity();
                }
        ).sum();

        saveOrder(order, productTotal, request.shippingFee(), request.shippingMethod(), request.paymentMethod());

        List<OrderDetail> orderDetails = new ArrayList<>();
        selectedProducts.forEach(product -> {
            ProductQuantity productQuantity = product.productQuantity();
            productQuantity.setQuantity(productQuantity.getQuantity() - product.quantity());
            productQuantityRepository.save(productQuantity);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setQuantity(product.quantity());
            orderDetails.add(orderDetail);
        });
        orderDetailRepository.saveAll(orderDetails);

        emailUtil.orderDetail(orderRepository.findById(order.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));

        return "Bạn đã thực hiện thành công đơn hàng";
    }

    private void saveOrder(Order order, double productTotal, double shippingFee, String shippingMethod, PaymentMethod paymentMethod) {
        order.setProductTotal(productTotal);
        order.setShippingFee(shippingFee);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);
        order.setId(idUtil.genId(productTotal + shippingFee, new Date()));
        orderRepository.save(order);
    }

    @Override
    public OrderResponse updateOrderForStaff(UpdateOrder request, String orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        existingOrder.setEmail(request.email());
        existingOrder.setName(request.name());
        existingOrder.setPhone(request.phone());
        existingOrder.setId(orderId);
        orderRepository.save(existingOrder);
        return objectMapperUtil.mapOrderResponse(existingOrder);
    }

    @Override
    public OrderResponse cancelOrderForStaff(String orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (existingOrder.getStatus() == OrderStatus.PENDING) {
            existingOrder.setStatus(OrderStatus.CANCELED);
            orderRepository.save(existingOrder);
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
        return productQuantityRepository.findByIdAndDeleted(productQuantityId, false)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_FOUND));
    }
}

