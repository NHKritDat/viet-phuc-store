package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.product.ProductCollectionResponse;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.product.*;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.product.*;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.ObjectMapperUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductCollectionRepository productCollectionRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductQuantityRepository productQuantityRepository;
    private final FeedbackRepository feedbackRepository;
    private final PageableUtil pageableUtil;
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public Page<SearchProduct> getProducts(String search, String[] sizes, String[] types, String[] collections,
                                           PageableRequest request) {
        Page<Product> products;
        if (sizes.length != 0 && types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductQuantities_ProductSize_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNotAndProductCollection_NameIn(
                            search,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndStatusNot(
                            search,
                            ProductStatus.DELETED,
                            pageableUtil.getPageable(Product.class, request)
                    );

        }
        return products.map(p -> objectMapperUtil.mapSearchProduct(
                p, feedbackRepository
                        .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(p.getId(), false)
                        .stream().mapToDouble(Feedback::getRating).average().orElse(0)
        ));
    }

    @Override
    public Page<SearchProduct> getProductsForStaff(String search, String[] sizes, String[] types, String[] collections,
                                                   PageableRequest request) {
        Page<Product> products;
        if (sizes.length != 0 && types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductQuantities_ProductSize_NameIn(
                            search,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductType_NameIn(
                            search,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndProductCollection_NameIn(
                            search,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else {
            products = productRepository
                    .findByNameContainsIgnoreCase(
                            search,
                            pageableUtil.getPageable(Product.class, request)
                    );
        }
        return products.map(p -> objectMapperUtil.mapSearchProduct(
                p, feedbackRepository
                        .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(p.getId(), false)
                        .stream().mapToDouble(Feedback::getRating).average().orElse(0)
        ));
    }

    @Override
    public ProductDetail getProduct(UUID id) {
        return objectMapperUtil.mapProductDetail(
                productRepository.findByIdAndStatusNot(id, ProductStatus.DELETED)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail createProduct(ModifyProductRequest request) {
        Product product = productRepository.save(
                objectMapperUtil.mapProduct(
                        request, new Product(), productTypeRepository
                                .findByIdAndDeleted(request.typeId(), false)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND)),
                        request.collectionId() != null ? productCollectionRepository
                                .findByIdAndDeleted(request.collectionId(), false)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
                                : null
                )
        );

        request.sizeQuantities().forEach((sizeId, quantity) -> {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct(product);
            productQuantity.setProductSize(productSizeRepository.findByIdAndDeleted(sizeId, false)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND)));
            productQuantity.setQuantity(quantity);
            productQuantityRepository.save(productQuantity);
        });

        return objectMapperUtil.mapProductDetail(
                productRepository.findById(product.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail updateProduct(UUID id, ModifyProductRequest request) {
        Product product = objectMapperUtil.mapProduct(
                request, productRepository
                        .findByIdAndStatusNot(id, ProductStatus.DELETED)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)),
                productTypeRepository
                        .findByIdAndDeleted(request.typeId(), false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND)),
                request.collectionId() != null ? productCollectionRepository
                        .findByIdAndDeleted(request.collectionId(), false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
                        : null
        );

        product.getProductQuantities().forEach(pq -> {
            if (!request.sizeQuantities().containsKey(pq.getProductSize().getId()))
                productQuantityRepository.delete(pq);
        });

        request.sizeQuantities().forEach((sizeId, quantity) -> {
            ProductQuantity productQuantity = product.getProductQuantities().stream()
                    .filter(pq -> pq.getProductSize().getId().equals(sizeId))
                    .findFirst().orElseGet(() -> {
                        ProductQuantity pq = new ProductQuantity();
                        pq.setProduct(product);
                        pq.setProductSize(productSizeRepository.findByIdAndDeleted(sizeId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND)));
                        return pq;
                    });
            productQuantity.setQuantity(quantity);
            productQuantityRepository.save(productQuantity);
        });

        return objectMapperUtil.mapProductDetail(productRepository.save(product));
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
    public Page<ProductCollectionResponse> getProductCollections(PageableRequest request) {
        return productCollectionRepository
                .findByDeleted(false, pageableUtil.getPageable(ProductCollection.class, request))
                .map(objectMapperUtil::mapProductCollectionResponse);
    }

    @Override
    public Page<ProductCollectionResponse> getProductCollectionsForStaff(PageableRequest request) {
        return productCollectionRepository
                .findAll(pageableUtil.getPageable(ProductCollection.class, request))
                .map(objectMapperUtil::mapProductCollectionResponse);
    }

    @Override
    public ProductCollectionResponse getProductCollection(UUID id) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
        );
    }

    @Override
    public ProductCollectionResponse createProductCollection(ModifyCollectionRequest request) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepository.save(
                        objectMapperUtil.mapProductCollection(request, new ProductCollection())
                )
        );
    }

    @Override
    public ProductCollectionResponse updateProductCollection(UUID id, ModifyCollectionRequest request) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepository.save(
                        objectMapperUtil.mapProductCollection(
                                request,
                                productCollectionRepository.findById(id)
                                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND)
                                        )
                        )
                )
        );
    }

    @Override
    public String deleteProductCollection(UUID id) {
        ProductCollection collection = productCollectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
        collection.setDeleted(true);
        productCollectionRepository.save(collection);
        return "Delete collection successfully.";
    }

    @Override
    public Page<FeedbackResponse> getFeedbacks(UUID productId, PageableRequest request) {
        return feedbackRepository
                .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                        productId, false, pageableUtil.getPageable(Feedback.class, request)
                ).map(objectMapperUtil::mapFeedbackResponse);
    }

    @Override
    public Page<FeedbackResponse> getFeedbacksForStaff(UUID productId, PageableRequest request) {
        return feedbackRepository
                .findByOrderDetail_ProductQuantity_Product_Id(
                        productId, pageableUtil.getPageable(Feedback.class, request)
                ).map(objectMapperUtil::mapFeedbackResponse);
    }

}
