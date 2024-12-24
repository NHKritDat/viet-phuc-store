package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProductService {
    Page<SearchProduct> getProducts(String search, String[] sizes, String[] types, String[] collections,
                                    PageableRequest request);

    Page<SearchProduct> getProductsForStaff(String search, String[] sizes, String[] types, String[] collections,
                                            PageableRequest request);

    ProductDetail getProduct(UUID id);

    ProductDetail createProduct(ModifyProductRequest request);

    ProductDetail updateProduct(UUID id, ModifyProductRequest request);

    String deleteProduct(UUID id);
}
