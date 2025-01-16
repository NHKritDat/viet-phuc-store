package com.nextrad.vietphucstore.services.impls.async;

import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderServiceImplAsync {
    private final FeedbackRepository feedbackRepository;
    private final ProductRepository productRepository;

    @Async
    public CompletableFuture<Double> avgRating(UUID productId) {
        return CompletableFuture.supplyAsync(() -> feedbackRepository.avgRatingByProductId(productId));
    }

    @Async
    public CompletableFuture<Void> assignRating(Product product, Double rating) {
        return CompletableFuture.runAsync(() -> {
            product.setAvgRating(rating);
            productRepository.save(product);
        });
    }
}
