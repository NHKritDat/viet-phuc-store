package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.product.*;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.product.*;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.product.*;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.ObjectMapperUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
    private final OrderDetailRepository orderDetailRepository;
    private final PageableUtil pageableUtil;
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public Page<SearchProduct> getProducts(String search, double minPrice, double maxPrice,
                                           String[] sizes, String[] types, String[] collections,
                                           PageableRequest request) {
        Page<Product> products;
        if (sizes.length != 0 && types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNot(
                            search,
                            minPrice,
                            maxPrice,
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
    public Page<SearchProductForStaff> getProductsForStaff(String search, double minPrice, double maxPrice,
                                                           String[] sizes, String[] types, String[] collections,
                                                           PageableRequest request) {
        Page<Product> products;
        if (sizes.length != 0 && types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0 && collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (collections.length != 0) {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else {
            products = productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetween(
                            search,
                            minPrice,
                            maxPrice,
                            pageableUtil.getPageable(Product.class, request)
                    );
        }
        return products.map(p -> objectMapperUtil.mapSearchProductForStaff(
                p, feedbackRepository
                        .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(p.getId(), false)
                        .stream().mapToDouble(Feedback::getRating).average().orElse(0)
        ));
    }

    @Override
    public ProductDetail getProduct(UUID id) {
        return objectMapperUtil.mapProductDetail(
                productRepository.findByIdAndStatusNotAndProductQuantities_Deleted(
                        id,
                        ProductStatus.DELETED,
                        false
                ).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail getProductForStaff(UUID id) {
        return objectMapperUtil.mapProductDetail(
                productRepository.findByIdAndProductQuantities_Deleted(id, false)
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
                        .findById(id)
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
            if (!request.sizeQuantities().containsKey(pq.getProductSize().getId())) {
                pq.setDeleted(true);
                productQuantityRepository.save(pq);
            }
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
            productQuantity.setDeleted(false);
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
        return "Sản phẩm đã được xóa.";
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
        return "Kích thước đã được xóa.";
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
        return "Loại sản phẩm đã được xóa.";
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
                productCollectionRepository.findByIdAndDeleted(id, false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
        );
    }

    @Override
    public ProductCollectionResponse getProductCollectionForStaff(UUID id) {
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
        return "Bộ sưu tập đã được xóa.";
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

    @Override
    public String reactiveProduct(UUID id) {
        Product product = productRepository.findByIdAndStatus(id, ProductStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(ProductStatus.IN_STOCK);
        productRepository.save(product);
        return "Bạn đã kích hoạt lại sản phẩm.";
    }

    @Override
    public String reactiveProductType(UUID id) {
        ProductType type = productTypeRepository.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setDeleted(false);
        productTypeRepository.save(type);
        return "Bạn đã kích hoạt lại loại sản phẩm.";
    }

    @Override
    public String reactiveProductCollection(UUID id) {
        ProductCollection collection = productCollectionRepository.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
        collection.setDeleted(false);
        productCollectionRepository.save(collection);
        return "Bạn đã kích hoạt lại bộ sưu tập sản phẩm.";
    }

    @Override
    public String reactiveProductSize(UUID id) {
        ProductSize size = productSizeRepository.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
        size.setDeleted(false);
        productSizeRepository.save(size);
        return "Bạn đã kích hoạt lại số đo sản phẩm.";
    }

    @Override
    public List<TopProduct> getTopProduct(PageableRequest pageableRequest) {
        List<Object[]> result = orderDetailRepository.findTopProduct(pageableRequest.size());
        return result.isEmpty() ?
                productRepository.findAll(pageableUtil.getPageable(Product.class, pageableRequest))
                        .map(p -> objectMapperUtil.mapTopProductResponse(
                                p, feedbackRepository
                                        .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                                                p.getId(),
                                                false
                                        ).stream().mapToDouble(Feedback::getRating).average().orElse(0)
                        )).stream().toList() :
                result.stream().map(p -> objectMapperUtil.mapTopProductResponse(
                        p, feedbackRepository
                                .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                                        UUID.nameUUIDFromBytes(p[0].toString().getBytes()),
                                        false)
                                .stream().mapToDouble(Feedback::getRating).average().orElse(0)
                )).toList();
    }

}
