package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.entities.product.*;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.product.*;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
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
            productQuantity.setProductSize(productSizeRepository.findByIdAndDeleted(sizeId, false)
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
        product.getProductQuantities().forEach(pq -> {
            if (!request.sizeQuantities().containsKey(pq.getProductSize().getId())) {
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
                        pq.setProductSize(productSizeRepository.findByIdAndDeleted(sizeId, false)
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

    @Override
    public Page<ProductSize> getSizes(PageableRequest request) {
        return productSizeRepository.findByDeleted(false,
                pageableUtil.getPageable(ProductSize.class, request));
    }

    @Override
    public Page<ProductSize> getSizesForStaff(PageableRequest request) {
        return productSizeRepository.findAll(pageableUtil.getPageable(ProductSize.class, request));
    }

    @Override
    public ProductSize getSize(UUID id) {
        return productSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
    }

    @Override
    public ProductSize createProductSize(ModifySizeRequest request) {
        ProductSize size = new ProductSize();
        size.setName(request.name());
        return productSizeRepository.save(size);
    }

    @Override
    public ProductSize updateProductSize(UUID id, ModifySizeRequest request) {
        ProductSize size = productSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
        size.setName(request.name());
        return productSizeRepository.save(size);
    }

    @Override
    public String deleteProductSize(UUID id) {
        ProductSize size = productSizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
        size.setDeleted(true);
        productSizeRepository.save(size);
        return "Delete size successfully.";
    }

    @Override
    public Page<ProductType> getProductTypes(PageableRequest request) {
        return productTypeRepository.findByDeleted(false,
                pageableUtil.getPageable(ProductType.class, request));
    }

    @Override
    public Page<ProductType> getProductTypesForStaff(PageableRequest request) {
        return productTypeRepository.findAll(pageableUtil.getPageable(ProductType.class, request));
    }

    @Override
    public ProductType getProductType(UUID id) {
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    @Override
    public ProductType createProductType(ModifyTypeRequest request) {
        ProductType type = new ProductType();
        type.setName(request.name());
        return productTypeRepository.save(type);
    }

    @Override
    public ProductType updateProductType(UUID id, ModifyTypeRequest request) {
        ProductType type = productTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setName(request.name());
        return productTypeRepository.save(type);
    }

    @Override
    public String deleteProductType(UUID id) {
        ProductType type = productTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setDeleted(true);
        productTypeRepository.save(type);
        return "Delete type successfully.";
    }

    @Override
    public Page<ProductCollection> getProductCollections(PageableRequest request) {
        return productCollectionRepository.findByDeleted(false,
                pageableUtil.getPageable(ProductCollection.class, request));
    }

    @Override
    public Page<ProductCollection> getProductCollectionsForStaff(PageableRequest request) {
        return productCollectionRepository.findAll(pageableUtil.getPageable(ProductCollection.class, request));
    }

    @Override
    public ProductCollection getProductCollection(UUID id) {
        return productCollectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
    }

    @Override
    public ProductCollection createProductCollection(ModifyCollectionRequest request) {
        ProductCollection collection = new ProductCollection();
        collection.setName(request.name());
        return productCollectionRepository.save(collection);
    }

    @Override
    public ProductCollection updateProductCollection(UUID id, ModifyCollectionRequest request) {
        ProductCollection collection = productCollectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
        collection.setName(request.name());
        return productCollectionRepository.save(collection);
    }

    @Override
    public String deleteProductCollection(UUID id) {
        ProductCollection collection = productCollectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
        collection.setDeleted(true);
        productCollectionRepository.save(collection);
        return "Delete collection successfully.";
    }

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

    private SearchProduct convertProductToSearchProduct(Product product) {
        return new SearchProduct(product.getId(), product.getName(), product.getUnitPrice(),
                Arrays
                        .asList(product.getPictures()
                                .substring(1, product.getPictures().length() - 1)
                                .split(", "))
                        .get(0));
    }

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
