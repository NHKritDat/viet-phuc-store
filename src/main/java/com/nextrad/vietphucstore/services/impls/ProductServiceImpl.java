package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.requests.api.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.api.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.api.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.api.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.inner.product.TopProductRequest;
import com.nextrad.vietphucstore.dtos.responses.api.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.api.product.*;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.product.*;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepo;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepo;
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
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductTypeRepo productTypeRepo;
    private final ProductCollectionRepo productCollectionRepo;
    private final ProductSizeRepo productSizeRepo;
    private final ProductQuantityRepo productQuantityRepo;
    private final FeedbackRepo feedbackRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final PageableUtil pageableUtil;
    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public Page<SearchProduct> getProducts(String search, double minPrice, double maxPrice,
                                           String[] sizes, String[] types, String[] collections,
                                           PageableRequest request) {
        Page<Product> products;
        if (sizes.length != 0 && types.length != 0 && collections.length != 0) {
            products = productRepo
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
            products = productRepo
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
            products = productRepo
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
            products = productRepo
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
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (collections.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNot(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            pageableUtil.getPageable(Product.class, request)
                    );

        }
        return products.map(p -> objectMapperUtil.mapSearchProduct(
                p, feedbackRepo
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
            products = productRepo
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
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0 && collections.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0 && collections.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (sizes.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (types.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else if (collections.length != 0) {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
        } else {
            products = productRepo
                    .findByNameContainsIgnoreCaseAndUnitPriceBetween(
                            search,
                            minPrice,
                            maxPrice,
                            pageableUtil.getPageable(Product.class, request)
                    );
        }
        return products.map(p -> objectMapperUtil.mapSearchProductForStaff(
                p, feedbackRepo
                        .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(p.getId(), false)
                        .stream().mapToDouble(Feedback::getRating).average().orElse(0)
        ));
    }

    @Override
    public ProductDetail getProduct(UUID id) {
        return objectMapperUtil.mapProductDetail(
                productRepo.findByIdAndStatusNotAndProductQuantities_Deleted(
                        id,
                        ProductStatus.DELETED,
                        false
                ).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail getProductForStaff(UUID id) {
        return objectMapperUtil.mapProductDetail(
                productRepo.findByIdAndProductQuantities_Deleted(id, false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail createProduct(ModifyProductRequest request) {
        Product product = productRepo.save(
                objectMapperUtil.mapProduct(
                        request, new Product(), productTypeRepo
                                .findByIdAndDeleted(request.typeId(), false)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND)),
                        request.collectionId() != null ? productCollectionRepo
                                .findByIdAndDeleted(request.collectionId(), false)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
                                : null
                )
        );

        request.sizeQuantities().forEach((sizeId, quantity) -> {
            ProductQuantity productQuantity = new ProductQuantity();
            productQuantity.setProduct(product);
            productQuantity.setProductSize(productSizeRepo.findByIdAndDeleted(sizeId, false)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND)));
            productQuantity.setQuantity(quantity);
            productQuantityRepo.save(productQuantity);
        });

        return objectMapperUtil.mapProductDetail(
                productRepo.findById(product.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail updateProduct(UUID id, ModifyProductRequest request) {
        Product product = objectMapperUtil.mapProduct(
                request, productRepo
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)),
                productTypeRepo
                        .findByIdAndDeleted(request.typeId(), false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND)),
                request.collectionId() != null ? productCollectionRepo
                        .findByIdAndDeleted(request.collectionId(), false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
                        : null
        );

        product.getProductQuantities().forEach(pq -> {
            if (!request.sizeQuantities().containsKey(pq.getProductSize().getId())) {
                pq.setDeleted(true);
                productQuantityRepo.save(pq);
            }
        });

        request.sizeQuantities().forEach((sizeId, quantity) -> {
            ProductQuantity productQuantity = product.getProductQuantities().stream()
                    .filter(pq -> pq.getProductSize().getId().equals(sizeId))
                    .findFirst().orElseGet(() -> {
                        ProductQuantity pq = new ProductQuantity();
                        pq.setProduct(product);
                        pq.setProductSize(productSizeRepo.findByIdAndDeleted(sizeId, false)
                                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND)));
                        return pq;
                    });
            productQuantity.setQuantity(quantity);
            productQuantity.setDeleted(false);
            productQuantityRepo.save(productQuantity);
        });

        return objectMapperUtil.mapProductDetail(productRepo.save(product));
    }

    @Override
    public String deleteProduct(UUID id) {
        Product product = productRepo.findByIdAndStatusNot(id, ProductStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(ProductStatus.DELETED);
        productRepo.save(product);
        return "Sản phẩm đã được xóa.";
    }

    @Override
    public Page<ProductSize> getSizes(PageableRequest request) {
        return productSizeRepo.findByDeleted(false,
                pageableUtil.getPageable(ProductSize.class, request));
    }

    @Override
    public Page<ProductSize> getSizesForStaff(PageableRequest request) {
        return productSizeRepo.findAll(pageableUtil.getPageable(ProductSize.class, request));
    }

    @Override
    public ProductSize getSize(UUID id) {
        return productSizeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
    }

    @Override
    public ProductSize createProductSize(ModifySizeRequest request) {
        ProductSize size = new ProductSize();
        size.setName(request.name());
        return productSizeRepo.save(size);
    }

    @Override
    public ProductSize updateProductSize(UUID id, ModifySizeRequest request) {
        ProductSize size = productSizeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
        size.setName(request.name());
        return productSizeRepo.save(size);
    }

    @Override
    public String deleteProductSize(UUID id) {
        ProductSize size = productSizeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
        size.setDeleted(true);
        productSizeRepo.save(size);
        return "Kích thước đã được xóa.";
    }

    @Override
    public Page<ProductType> getProductTypes(PageableRequest request) {
        return productTypeRepo.findByDeleted(false,
                pageableUtil.getPageable(ProductType.class, request));
    }

    @Override
    public Page<ProductType> getProductTypesForStaff(PageableRequest request) {
        return productTypeRepo.findAll(pageableUtil.getPageable(ProductType.class, request));
    }

    @Override
    public ProductType getProductType(UUID id) {
        return productTypeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
    }

    @Override
    public ProductType createProductType(ModifyTypeRequest request) {
        ProductType type = new ProductType();
        type.setName(request.name());
        return productTypeRepo.save(type);
    }

    @Override
    public ProductType updateProductType(UUID id, ModifyTypeRequest request) {
        ProductType type = productTypeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setName(request.name());
        return productTypeRepo.save(type);
    }

    @Override
    public String deleteProductType(UUID id) {
        ProductType type = productTypeRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setDeleted(true);
        productTypeRepo.save(type);
        return "Loại sản phẩm đã được xóa.";
    }

    @Override
    public Page<ProductCollectionResponse> getProductCollections(PageableRequest request) {
        return productCollectionRepo
                .findByDeleted(false, pageableUtil.getPageable(ProductCollection.class, request))
                .map(objectMapperUtil::mapProductCollectionResponse);
    }

    @Override
    public Page<ProductCollectionResponse> getProductCollectionsForStaff(PageableRequest request) {
        return productCollectionRepo
                .findAll(pageableUtil.getPageable(ProductCollection.class, request))
                .map(objectMapperUtil::mapProductCollectionResponse);
    }

    @Override
    public ProductCollectionResponse getProductCollection(UUID id) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepo.findByIdAndDeleted(id, false)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
        );
    }

    @Override
    public ProductCollectionResponse getProductCollectionForStaff(UUID id) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepo.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
        );
    }

    @Override
    public ProductCollectionResponse createProductCollection(ModifyCollectionRequest request) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepo.save(
                        objectMapperUtil.mapProductCollection(request, new ProductCollection())
                )
        );
    }

    @Override
    public ProductCollectionResponse updateProductCollection(UUID id, ModifyCollectionRequest request) {
        return objectMapperUtil.mapProductCollectionResponse(
                productCollectionRepo.save(
                        objectMapperUtil.mapProductCollection(
                                request,
                                productCollectionRepo.findById(id)
                                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND)
                                        )
                        )
                )
        );
    }

    @Override
    public String deleteProductCollection(UUID id) {
        ProductCollection collection = productCollectionRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
        collection.setDeleted(true);
        productCollectionRepo.save(collection);
        return "Bộ sưu tập đã được xóa.";
    }

    @Override
    public Page<FeedbackResponse> getFeedbacks(UUID productId, PageableRequest request) {
        return feedbackRepo
                .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                        productId, false, pageableUtil.getPageable(Feedback.class, request)
                ).map(objectMapperUtil::mapFeedbackResponse);
    }

    @Override
    public Page<FeedbackResponse> getFeedbacksForStaff(UUID productId, PageableRequest request) {
        return feedbackRepo
                .findByOrderDetail_ProductQuantity_Product_Id(
                        productId, pageableUtil.getPageable(Feedback.class, request)
                ).map(objectMapperUtil::mapFeedbackResponse);
    }

    @Override
    public String reactiveProduct(UUID id) {
        Product product = productRepo.findByIdAndStatus(id, ProductStatus.DELETED)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(ProductStatus.IN_STOCK);
        productRepo.save(product);
        return "Bạn đã kích hoạt lại sản phẩm.";
    }

    @Override
    public String reactiveProductType(UUID id) {
        ProductType type = productTypeRepo.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setDeleted(false);
        productTypeRepo.save(type);
        return "Bạn đã kích hoạt lại loại sản phẩm.";
    }

    @Override
    public String reactiveProductCollection(UUID id) {
        ProductCollection collection = productCollectionRepo.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND));
        collection.setDeleted(false);
        productCollectionRepo.save(collection);
        return "Bạn đã kích hoạt lại bộ sưu tập sản phẩm.";
    }

    @Override
    public String reactiveProductSize(UUID id) {
        ProductSize size = productSizeRepo.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_SIZE_NOT_FOUND));
        size.setDeleted(false);
        productSizeRepo.save(size);
        return "Bạn đã kích hoạt lại số đo sản phẩm.";
    }

    @Override
    public List<TopProduct> getTopProduct(PageableRequest pageableRequest) {
        List<TopProductRequest> result = orderDetailRepo.findTopProduct(pageableRequest.size());
        return result.isEmpty() ?
                productRepo.findAll(pageableUtil.getPageable(Product.class, pageableRequest))
                        .map(p -> objectMapperUtil.mapTopProductResponse(
                                p, feedbackRepo
                                        .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                                                p.getId(),
                                                false
                                        ).stream().mapToDouble(Feedback::getRating).average().orElse(0)
                        )).stream().toList() :
                result.stream().map(p -> objectMapperUtil.mapTopProductResponse(
                        p, feedbackRepo
                                .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                                        p.id(),
                                        false)
                                .stream().mapToDouble(Feedback::getRating).average().orElse(0)
                )).toList();
    }

}
