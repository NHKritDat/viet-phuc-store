package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.order.CreateOrder;
import com.nextrad.vietphucstore.dtos.requests.order.FeedbackRequest;
import com.nextrad.vietphucstore.dtos.requests.order.ModifyCartRequest;
import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.order.CartInfo;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.order.OrderHistory;
import com.nextrad.vietphucstore.entities.order.Cart;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.CartRepository;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.product.ProductQuantityRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.OrderService;
import com.nextrad.vietphucstore.utils.IdUtil;
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
public class OrderServiceImplement implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final FeedbackRepository feedbackRepository;
    private final PageableUtil pageableUtil;
    private final UserRepository userRepository;
    private final ProductQuantityRepository productQuantityRepository;
    private final IdUtil idUtil;

    @Override
    public String addToCart(ModifyCartRequest request) {
        Optional<Cart> cart = cartRepository.findByProductQuantity_Id(request.productQuantityId());
        if (cart.isPresent()) {
            cart.get().setQuantity(
                    Math.min(cart.get().getQuantity() + request.quantity(), cart.get().getQuantity())
            );
            cartRepository.save(cart.get());
        } else {
            Cart newCart = new Cart();
            newCart.setUser(userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            ).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
            ProductQuantity productQuantity = productQuantityRepository.findById(
                    request.productQuantityId()
            ).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_QUANTITY_NOT_FOUND));
            newCart.setProductQuantity(productQuantity);
            newCart.setQuantity(Math.min(request.quantity(), productQuantity.getQuantity()));
            cartRepository.save(newCart);
        }
        return "Add to cart successfully";
    }

    @Override
    public String removeFromCart(ModifyCartRequest request) {
        Optional<Cart> cart = cartRepository.findByProductQuantity_Id(request.productQuantityId());
        if (cart.isEmpty())
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        else {
            cart.get().setQuantity(
                    Math.max(cart.get().getQuantity() - request.quantity(), 0)
            );
            if (cart.get().getQuantity() == 0)
                cartRepository.delete(cart.get());
            else
                cartRepository.save(cart.get());
        }
        return "Remove from cart successfully";
    }

    @Override
    public Page<CartInfo> getCartInfo(PageableRequest request) {
        Page<Cart> carts = cartRepository.findByUser_Email(
                SecurityContextHolder.getContext().getAuthentication().getName(),
                pageableUtil.getPageable(Cart.class, request)
        );
        return carts.map(cart -> new CartInfo(
                cart.getProductQuantity().getId(),
                cart.getProductQuantity().getProduct().getName(),
                cart.getProductQuantity().getProduct().getUnitPrice(),
                cart.getProductQuantity().getProductSize().getName(),
                cart.getQuantity()
        ));
    }

    @Override
    public String checkout(CreateOrder request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Order order = new Order();
        order.setUser(userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        order.setEmail(request.email());
        order.setName(request.name());
        order.setProvince(request.province());
        order.setDistrict(request.district());
        order.setAddress(request.address());
        order.setPhone(request.phone());
        List<Cart> carts = cartRepository.findByUser_Email(currentUserEmail);
        if (carts.isEmpty())
            throw new AppException(ErrorCode.CART_EMPTY);
        double productTotal = carts.stream().mapToDouble(
                cart -> cart.getProductQuantity().getProduct().getUnitPrice() * cart.getQuantity()
        ).sum();
        order.setProductTotal(productTotal);
        order.setShippingFee(request.shippingFee());
        order.setPaymentMethod(request.paymentMethod());
        order.setId(idUtil.genId(productTotal + request.shippingFee(), new Date()));
        orderRepository.save(order);

        carts.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProductQuantity(cart.getProductQuantity());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetailRepository.save(orderDetail);
            cartRepository.delete(cart);
        });

        return "Order successfully";
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
        return "Update order status successfully";
    }

    @Override
    public String previousStatus(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getStatus() == OrderStatus.DELIVERED)
            order.setStatus(OrderStatus.IN_TRANSIT);
        else if (order.getStatus() == OrderStatus.IN_TRANSIT)
            order.setStatus(OrderStatus.AWAITING_DELIVERY);
        else if (order.getStatus() == OrderStatus.AWAITING_DELIVERY)
            order.setStatus(OrderStatus.AWAITING_PICKUP);
        else if (order.getStatus() == OrderStatus.AWAITING_PICKUP)
            order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);
        return "Reverse order status successfully";
    }

    @Override
    public Page<OrderHistory> getHistoryOrders(PageableRequest request) {
        Page<OrderDetail> orderDetails = orderDetailRepository
                .findByOrder_User_EmailAndOrder_Status(
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        OrderStatus.DELIVERED,
                        pageableUtil.getPageable(OrderDetail.class, request)
                );
        return orderDetails.map(this::toOrderHistory);
    }

    @Override
    public Page<OrderHistory> getOrdersForStaff(PageableRequest request) {
        Page<OrderDetail> orderDetails = orderDetailRepository.findAll(
                pageableUtil.getPageable(OrderDetail.class, request)
        );
        return orderDetails.map(this::toOrderHistory);
    }

    @Override
    public FeedbackResponse doFeedback(UUID orderDetailId, FeedbackRequest request) {
        Optional<Feedback> feedback = feedbackRepository.findByOrderDetail_Id(orderDetailId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (feedback.isPresent()) {
            if (!feedback.get().getOrderDetail().getOrder().getUser().getEmail().equals(email))
                throw new AppException(ErrorCode.NO_PERMISSION);
            return setFeedbackResponse(setFeedback(request, feedback.get()));
        } else {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
            if (!orderDetail.getOrder().getUser().getEmail().equals(email))
                throw new AppException(ErrorCode.NO_PERMISSION);
            Feedback newFeedback = new Feedback();
            newFeedback.setOrderDetail(orderDetail);
            return setFeedbackResponse(setFeedback(request, newFeedback));
        }
    }

    @Override
    public FeedbackResponse getFeedback(UUID orderDetailId) {
        Feedback feedback = feedbackRepository.findByOrderDetail_Id(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.FEEDBACK_NOT_FOUND));
        return setFeedbackResponse(feedback);
    }

    private Feedback setFeedback(FeedbackRequest request, Feedback feedback) {
        feedback.setRating(request.rating());
        feedback.setContent(request.content());
        return feedbackRepository.save(feedback);
    }

    private FeedbackResponse setFeedbackResponse(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(),
                feedback.getOrderDetail().getProductQuantity().getProduct().getName(),
                feedback.getOrderDetail().getProductQuantity().getProduct().getPictures()
                        .substring(1,
                                feedback.getOrderDetail().getProductQuantity().getProduct()
                                        .getPictures().length() - 1)
                        .split(", ")[0],
                feedback.getContent(),
                feedback.getRating(),
                feedback.getOrderDetail().getOrder().getUser().getName(),
                feedback.getCreatedDate()
        );
    }

    private OrderHistory toOrderHistory(OrderDetail od) {
        return new OrderHistory(
                od.getOrder().getId(),
                od.getId(),
                od.getProductQuantity().getProduct().getPictures()
                        .substring(1,
                                od.getProductQuantity().getProduct()
                                        .getPictures().length() - 1)
                        .split(", ")[0],
                od.getProductQuantity().getProduct().getName(),
                od.getOrder().getPaymentMethod(),
                od.getProductQuantity().getProductSize().getName(),
                od.getQuantity(),
                od.getProductQuantity().getProduct().getUnitPrice()
        );
    }
}
