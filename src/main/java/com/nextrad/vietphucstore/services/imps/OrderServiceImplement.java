package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.order.ModifyCartRequest;
import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.order.CartInfo;
import com.nextrad.vietphucstore.entities.order.Cart;
import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.CartRepository;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.order.OrderRepository;
import com.nextrad.vietphucstore.repositories.product.ProductQuantityRepository;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.OrderService;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
