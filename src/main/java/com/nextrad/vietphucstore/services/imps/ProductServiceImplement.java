package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.entities.product.ProductQuantity;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.product.*;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductCollectionRepository productCollectionRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductQuantityRepository productQuantityRepository;
    private final PageableUtil pageableUtil;

    @Override
    public Page<SearchProduct> getProducts(String search, String[] sizes, String[] types, String[] collections,
                                           PageableRequest request) {
        Page<Product> products = productRepository
                .findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                        search,
                        ProductStatus.DELETED,
                        Arrays.asList(types),
                        Arrays.asList(collections),
                        Arrays.asList(sizes),
                        pageableUtil.getPageable(Product.class, request)
                );
        return products.map(this::convertProductToSearchProduct);
    }

    @Override
    public Page<SearchProduct> getProductsForStaff(String search, String[] sizes, String[] types, String[] collections,
                                                   PageableRequest request) {
        Page<Product> products = productRepository
                .findByNameContainsIgnoreCaseAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                        search,
                        Arrays.asList(types),
                        Arrays.asList(collections),
                        Arrays.asList(sizes),
                        pageableUtil.getPageable(Product.class, request)
                );
        return products.map(this::convertProductToSearchProduct);
    }

    @Override
    public ProductDetail getProduct(UUID id) {
        Product product = productRepository.findByIdAndStatusNot(id, ProductStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return convertProductToProductDetail(product);
    }

    @Override
    public ProductDetail createProduct(ModifyProductRequest request) {
        Product product = setProduct(request, new Product());
        productRepository.save(product);
        request.sizeQuantities().forEach((sizeId, quantity) -> {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct(product);
            productQuantity.setProductSize(productSizeRepository.findById(sizeId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND)));
            productQuantity.setQuantity(quantity);
            productQuantityRepository.save(productQuantity);
        });
        return convertProductToProductDetail(product);
    }

    @Override
    public ProductDetail updateProduct(UUID id, ModifyProductRequest request) {
        Product product = setProduct(request, productRepository.findByIdAndStatusNot(id, ProductStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));
        product.getProductQuantities().forEach(pq->{
            if(!request.sizeQuantities().containsKey(pq.getProductSize().getId())){
                productQuantityRepository.delete(pq);
            }
        });
        request.sizeQuantities().forEach((sizeId, quantity) -> {
            ProductQuantity productQuantity = product.getProductQuantities().stream()
                    .filter(pq -> pq.getProductSize().getId().equals(sizeId))
                    .findFirst()
                    .orElseGet(() -> {
                        ProductQuantity pq = new ProductQuantity();
                        pq.setProduct(product);
                        pq.setProductSize(productSizeRepository.findById(sizeId)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND)));
                        return pq;
                    });
            productQuantity.setQuantity(quantity);
            productQuantityRepository.save(productQuantity);
        });
        productRepository.save(product);
        return convertProductToProductDetail(product);
    }

    @Override
    public String deleteProduct(UUID id) {
        Product product = productRepository.findByIdAndStatusNot(id, ProductStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);
        return "Delete product successfully.";
    }

    @NotNull
    private ProductDetail convertProductToProductDetail(Product product) {
        return new ProductDetail(
                product.getId(), product.getName(), product.getDescription(), product.getUnitPrice(),
                Arrays
                        .asList(product.getPictures()
                                .substring(1, product.getPictures().length() - 1)
                                .split(", ")),
                product.getStatus(), product.getProductCollection().getName(), product.getProductType().getName(),
                product.getProductQuantities().stream().collect(
                        Collectors.toMap(
                                pq -> pq.getProductSize().getName(),
                                ProductQuantity::getQuantity
                        )
                )
        );
    }

    @NotNull
    private SearchProduct convertProductToSearchProduct(Product product) {
        return new SearchProduct(product.getId(), product.getName(), product.getUnitPrice(),
                Arrays
                        .asList(product.getPictures()
                                .substring(1, product.getPictures().length() - 1)
                                .split(", "))
                        .get(0));
    }

    @NotNull
    private Product setProduct(ModifyProductRequest request, Product product) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setUnitPrice(request.unitPrice());
        product.setPictures(request.pictures().toString());
        product.setStatus(request.status());
        product.setProductType(productTypeRepository.findByIdAndDeleted(request.typeId(), false)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND)));
        product.setProductCollection(productCollectionRepository.findByIdAndDeleted(request.collectionId(), false)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND)));
        return product;
    }

}
