package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.product.*;
import com.nextrad.vietphucstore.entities.product.ProductSize;
import com.nextrad.vietphucstore.entities.product.ProductType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<SearchProduct> getProducts(String search, double minPrice, double maxPrice,
                                    String[] sizes, String[] types, String[] collections,
                                    PageableRequest request);

    Page<SearchProductForStaff> getProductsForStaff(String search, double minPrice, double maxPrice,
                                                    String[] sizes, String[] types, String[] collections,
                                                    PageableRequest request);

    ProductDetail getProduct(UUID id);

    ProductDetail getProductForStaff(UUID id);

    ProductDetail createProduct(ModifyProductRequest request);

    ProductDetail updateProduct(UUID id, ModifyProductRequest request);

    String deleteProduct(UUID id);

    Page<ProductSize> getSizes(PageableRequest request);

    Page<ProductSize> getSizesForStaff(PageableRequest request);

    ProductSize getSize(UUID id);

    ProductSize createProductSize(ModifySizeRequest request);

    ProductSize updateProductSize(UUID id, ModifySizeRequest request);

    String deleteProductSize(UUID id);

    Page<ProductType> getProductTypes(PageableRequest request);

    Page<ProductType> getProductTypesForStaff(PageableRequest request);

    ProductType getProductType(UUID id);

    ProductType createProductType(ModifyTypeRequest request);

    ProductType updateProductType(UUID id, ModifyTypeRequest request);

    String deleteProductType(UUID id);

    Page<ProductCollectionResponse> getProductCollections(PageableRequest request);

    Page<ProductCollectionResponse> getProductCollectionsForStaff(PageableRequest request);

    ProductCollectionResponse getProductCollection(UUID id);

    ProductCollectionResponse getProductCollectionForStaff(UUID id);

    ProductCollectionResponse createProductCollection(ModifyCollectionRequest request);

    ProductCollectionResponse updateProductCollection(UUID id, ModifyCollectionRequest request);

    String deleteProductCollection(UUID id);

    Page<FeedbackResponse> getFeedbacks(UUID productId, PageableRequest request);

    Page<FeedbackResponse> getFeedbacksForStaff(UUID productId, PageableRequest request);

    String reactiveProduct(UUID id);

    String reactiveProductType(UUID id);

    String reactiveProductCollection(UUID id);

    String reactiveProductSize(UUID id);

    List<TopProduct> getTopProduct(PageableRequest pageableRequest);
}
