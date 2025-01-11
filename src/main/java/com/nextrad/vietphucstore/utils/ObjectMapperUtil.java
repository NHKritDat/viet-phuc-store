package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.dtos.requests.order.FeedbackRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.responses.order.*;
import com.nextrad.vietphucstore.dtos.responses.product.*;
import com.nextrad.vietphucstore.dtos.responses.user.SearchUser;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.dtos.responses.user.UserDetail;
import com.nextrad.vietphucstore.entities.order.Cart;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.order.Order;
import com.nextrad.vietphucstore.entities.order.OrderDetail;
import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.entities.product.ProductCollection;
import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.entities.product.ProductType;
import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.order.OrderStatus;
import com.nextrad.vietphucstore.enums.order.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ObjectMapperUtil {
    private final ImagesUtil imagesUtil;

    public OrderHistory mapOrderHistory(OrderDetail od, boolean feedback) {
        return new OrderHistory(
                od.getOrder().getId(),
                od.getId(),
                imagesUtil.convertStringToImages(od
                        .getProductQuantity().getProduct().getPictures()).get(0),
                od.getProductQuantity().getProduct().getName(),
                od.getOrder().getPaymentMethod(),
                od.getProductQuantity().getProductSize().getName(),
                od.getQuantity(),
                od.getProductQuantity().getProduct().getUnitPrice(),
                od.getOrder().getShippingFee(),
                feedback
        );
    }

    public FeedbackResponse mapFeedbackResponse(Feedback feedback) {
        return new FeedbackResponse(
                feedback.getId(), feedback.getOrderDetail().getProductQuantity().getProduct().getName(),
                imagesUtil.convertStringToImages(feedback.getOrderDetail()
                        .getProductQuantity().getProduct().getPictures()).get(0),
                feedback.getContent(), feedback.getRating(),
                feedback.getOrderDetail().getOrder().getUser().getName(), feedback.getCreatedDate(),
                feedback.getOrderDetail().getProductQuantity().getProduct().getId()
        );
    }

    public Feedback mapFeedback(FeedbackRequest request, Feedback feedback) {
        feedback.setRating(request.rating());
        feedback.setContent(request.content());
        return feedback;
    }

    public CartInfo mapCartInfo(Cart cart) {
        return new CartInfo(
                cart.getProductQuantity().getId(),
                cart.getProductQuantity().getProduct().getName(),
                imagesUtil.convertStringToImages(cart.getProductQuantity().getProduct().getPictures()).get(0),
                cart.getProductQuantity().getProduct().getUnitPrice(),
                cart.getProductQuantity().getProduct().getWeight(),
                cart.getProductQuantity().getProductSize().getName(),
                cart.getQuantity(),
                cart.getProductQuantity().getQuantity()
        );
    }

    public UserDetail mapUserDetail(User user) {
        return new UserDetail(
                user.getId(), user.getName(), user.getDob(), user.getGender(), user.getEmail(),
                user.getProvince(), user.getDistrict(), user.getAddress(), user.getPhone(),
                user.getAvatar(), user.getRole(), user.getStatus(),
                user.getCreatedBy(), user.getCreatedDate(), user.getUpdatedBy(), user.getUpdatedDate()
        );
    }

    public SearchUser mapSearchUser(User user) {
        return new SearchUser(user.getId(), user.getName(), user.getEmail(), user.getAvatar(), user.getStatus());
    }

    public TokenResponse mapTokenResponse(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }

    public ProductCollection mapProductCollection(ModifyCollectionRequest request, ProductCollection collection) {
        collection.setName(request.name());
        collection.setDescription(request.description());
        collection.setImages(request.images().toString());
        return collection;
    }

    public SearchProduct mapSearchProduct(Product product, double rating) {
        return new SearchProduct(
                product.getId(), product.getName(), product.getUnitPrice(),
                imagesUtil.convertStringToImages(product.getPictures()).get(0),
                rating
        );
    }

    public SearchProductForStaff mapSearchProductForStaff(Product product, double rating) {
        return new SearchProductForStaff(
                product.getId(), product.getName(), product.getUnitPrice(),
                imagesUtil.convertStringToImages(product.getPictures()).get(0),
                rating, product.getStatus()
        );
    }

    public Product mapProduct(ModifyProductRequest request, Product product, ProductType type, ProductCollection collection) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setUnitPrice(request.unitPrice());
        product.setPictures(request.pictures().toString());
        product.setWeight(request.weight());
        product.setStatus(request.status());
        product.setProductType(type);
        product.setProductCollection(collection);
        return product;
    }

    public ProductDetail mapProductDetail(Product product) {
        return new ProductDetail(
                product.getId(), product.getName(), product.getDescription(), product.getUnitPrice(),
                imagesUtil.convertStringToImages(product.getPictures()), product.getWeight(), product.getStatus(),
                product.getProductCollection() != null ? product.getProductCollection().getName() : null,
                product.getProductType().getName(),
                product.getProductQuantities().stream().collect(Collectors.toMap(
                        ProductQuantity::getId,
                        productQuantity -> new SizeQuantityResponse(
                                productQuantity.getProductSize().getName(),
                                productQuantity.getQuantity()
                        )
                ))
        );
    }

    public ProductCollectionResponse mapProductCollectionResponse(ProductCollection collection) {
        return new ProductCollectionResponse(
                collection.getId(), collection.getName(), collection.getDescription(),
                imagesUtil.convertStringToImages(collection.getImages()), collection.isDeleted()
        );
    }

    public SearchOrder mapSearchOrder(Order order) {
        return new SearchOrder(
                order.getId(), order.getEmail(), order.getProductTotal(),
                order.getShippingFee(), order.getShippingMethod(), order.getPaymentMethod(), order.getStatus()
        );
    }

    public OrderResponse mapOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(), order.getEmail(), order.getName(),
                order.getAddress() + ", " + order.getDistrict() + ", " + order.getProvince(),
                order.getPhone(), order.getProductTotal(), order.getShippingFee(), order.getShippingMethod(),
                order.getPaymentMethod(), order.getStatus(),
                order.getOrderDetails().stream().collect(Collectors.toMap(
                        OrderDetail::getId,
                        od -> new OrderDetailResponse(
                                od.getProductQuantity().getProduct().getName(),
                                imagesUtil.convertStringToImages(od.getProductQuantity().getProduct().getPictures()),
                                od.getProductQuantity().getProductSize().getName(),
                                od.getQuantity(),
                                od.getProductQuantity().getProduct().getUnitPrice()
                        )
                ))
        );
    }

    public CurrentOrderHistory mapCurrentOrderHistory(Order order) {
        return new CurrentOrderHistory(
                order.getId(), order.getName(),
                order.getAddress() + ", " + order.getDistrict() + ", " + order.getProvince(),
                order.getPaymentMethod(), order.getStatus(), order.getProductTotal() + order.getShippingFee()
        );
    }

    public TransactionsResponse mapTransactionsResponse(Order order) {
        boolean paymentStatus;
        Date paymentDate;
        if (order.getPaymentMethod().equals(PaymentMethod.QR)) {
            paymentStatus = true;
            paymentDate = order.getCreatedDate();
        } else if (order.getPaymentMethod().equals(PaymentMethod.COD) && order.getStatus().equals(OrderStatus.DELIVERED)) {
            paymentStatus = true;
            paymentDate = order.getUpdatedDate();
        } else {
            paymentStatus = false;
            paymentDate = null;
        }
        return new TransactionsResponse(
                order.getPaymentMethod(), paymentStatus, paymentDate,
                order.getId(), order.getCreatedDate(), order.getShippingFee(),
                order.getProductTotal() + order.getShippingFee()
        );
    }

    public TopProduct mapTopProductResponse(Object[] request, double rating) {
        return new TopProduct(
                UUID.nameUUIDFromBytes(request[0].toString().getBytes()),
                request[1].toString(),
                Double.parseDouble(request[2].toString()),
                imagesUtil.convertStringToImages(request[3].toString()).get(0),
                rating,
                Integer.parseInt(request[4].toString())
        );
    }

    public TopProduct mapTopProductResponse(Product request, double rating) {
        return new TopProduct(
                request.getId(),
                request.getName(),
                request.getUnitPrice(),
                imagesUtil.convertStringToImages(request.getPictures()).get(0),
                rating,
                0
        );
    }
}
