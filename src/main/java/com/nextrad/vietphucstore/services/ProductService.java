package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.entities.product.ProductCollection;
import com.nextrad.vietphucstore.entities.product.ProductSize;
import com.nextrad.vietphucstore.entities.product.ProductType;
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

    Page<ProductCollection> getProductCollections(PageableRequest request);

    Page<ProductCollection> getProductCollectionsForStaff(PageableRequest request);

    ProductCollection getProductCollection(UUID id);

    ProductCollection createProductCollection(ModifyCollectionRequest request);

    ProductCollection updateProductCollection(UUID id, ModifyCollectionRequest request);

    String deleteProductCollection(UUID id);
}
