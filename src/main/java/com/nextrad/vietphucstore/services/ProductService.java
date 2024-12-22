package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<SearchProduct> getProducts(String search, String[] sizes, String[] types, String[] collections,
                                    PageableRequest request);

    Page<SearchProduct> getProductsForStaff(String search, String[] sizes, String[] types, String[] collections,
                                            PageableRequest request);
}
