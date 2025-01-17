package com.nextrad.vietphucstore.services.impls;

import com.nextrad.vietphucstore.dtos.requests.api.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.api.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.api.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.api.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.requests.inner.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.inner.product.TopProductRequest;
import com.nextrad.vietphucstore.dtos.responses.api.order.FeedbackSummary;
import com.nextrad.vietphucstore.dtos.responses.api.product.*;
import com.nextrad.vietphucstore.entities.order.Feedback;
import com.nextrad.vietphucstore.entities.product.*;
import com.nextrad.vietphucstore.enums.error.ErrorCode;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.exceptions.AppException;
import com.nextrad.vietphucstore.repositories.order.FeedbackRepository;
import com.nextrad.vietphucstore.repositories.order.OrderDetailRepository;
import com.nextrad.vietphucstore.repositories.product.*;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.BinaryUtil;
import com.nextrad.vietphucstore.utils.ObjectMapperUtil;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepo;
    private final ProductCollectionRepository productCollectionRepository;
    private final ProductSizeRepository productSizeRepository;
    private final ProductQuantityRepository productQuantityRepository;
    private final FeedbackRepository feedbackRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PageableUtil pageableUtil;
    private final ObjectMapperUtil objectMapperUtil;
    private final BinaryUtil binaryUtil;

    @Override
    public Page<SearchProduct> getProducts(String search, double minPrice, double maxPrice,
                                           String[] sizes, String[] types, String[] collections,
                                           PageableRequest request) {
        //Check sizes, types, collections empty
        int check = binaryUtil.checkBinary(sizes, types, collections);
        //Switch case sizes, types, collections empty
        Page<Product> products = switch (check) {
            case 111 -> productRepository
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
            case 110 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 101 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 100 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 11 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 10 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductType_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 1 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNotAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
            default -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndStatusNot(
                            search,
                            minPrice,
                            maxPrice,
                            ProductStatus.DELETED,
                            pageableUtil.getPageable(Product.class, request)
                    );
        };
        //Async map to SearchProduct
        Page<CompletableFuture<SearchProduct>> futures = products.map(objectMapperUtil::mapSearchProduct);
        return CompletableFuture.allOf(futures.getContent().toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.map(CompletableFuture::join)).join();
    }

    @Override
    public Page<SearchProductForStaff> getProductsForStaff(String search, double minPrice, double maxPrice,
                                                           String[] sizes, String[] types, String[] collections,
                                                           PageableRequest request) {
        //Check sizes, types, collections empty
        int check = binaryUtil.checkBinary(sizes, types, collections);
        //Switch case sizes, types, collections empty
        Page<Product> products = switch (check) {
            case 111 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 110 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 101 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(collections),
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 11 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameInAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 100 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductQuantities_ProductSize_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(sizes),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 10 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductType_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(types),
                            pageableUtil.getPageable(Product.class, request)
                    );
            case 1 -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetweenAndProductCollection_NameIn(
                            search,
                            minPrice,
                            maxPrice,
                            Arrays.asList(collections),
                            pageableUtil.getPageable(Product.class, request)
                    );
            default -> productRepository
                    .findByNameContainsIgnoreCaseAndUnitPriceBetween(
                            search,
                            minPrice,
                            maxPrice,
                            pageableUtil.getPageable(Product.class, request)
                    );
        };
        Page<CompletableFuture<SearchProductForStaff>> futures = products.map(objectMapperUtil::mapSearchProductForStaff);
        return CompletableFuture.allOf(futures.getContent().toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.map(CompletableFuture::join)).join();
    }

    @Override
    public ProductDetail getProduct(UUID id) {
        return objectMapperUtil.mapProductDetailForCustomer(
                productRepository.findByIdAndStatusNot(id, ProductStatus.DELETED)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail getProductForStaff(UUID id) {
        return objectMapperUtil.mapProductDetail(
                productRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
        );
    }

    @Override
    public ProductDetail createProduct(ModifyProductRequest request) {
        Product product = productRepository.save(
                objectMapperUtil.mapProduct(
                        request, new Product(), productTypeRepo
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
                productTypeRepo
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
        return productSizeRepository.findByDeleted(false, pageableUtil.getPageable(ProductSize.class, request));
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
        return productTypeRepo.findByDeleted(false, pageableUtil.getPageable(ProductType.class, request));
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
                                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_COLLECTION_NOT_FOUND))
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
    public Page<ProductFeedback> getFeedbacks(UUID productId, PageableRequest request) {
        Page<CompletableFuture<ProductFeedback>> futures = feedbackRepository
                .findByOrderDetail_ProductQuantity_Product_IdAndDeleted(
                        productId, false, pageableUtil.getPageable(Feedback.class, request)
                ).map(objectMapperUtil::mapProductFeedback);
        return CompletableFuture.allOf(futures.getContent().toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.map(CompletableFuture::join)).join();
    }

    @Override
    public Page<ProductFeedback> getFeedbacksForStaff(UUID productId, PageableRequest request) {
        Page<CompletableFuture<ProductFeedback>> futures = feedbackRepository
                .findByOrderDetail_ProductQuantity_Product_Id(
                        productId, pageableUtil.getPageable(Feedback.class, request)
                ).map(objectMapperUtil::mapProductFeedback);
        return CompletableFuture.allOf(futures.getContent().toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.map(CompletableFuture::join)).join();
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
        ProductType type = productTypeRepo.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_TYPE_NOT_FOUND));
        type.setDeleted(false);
        productTypeRepo.save(type);
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
        List<TopProductRequest> result = orderDetailRepository.findTopProduct(pageableRequest.size());
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
                                        p.id(),
                                        false)
                                .stream().mapToDouble(Feedback::getRating).average().orElse(0)
                )).toList();
    }

    @Override
    public FeedbackSummary getFeedbackSummary(UUID id) {
        return objectMapperUtil.mapFeedbackSummary(
                feedbackRepository.findByOrderDetail_ProductQuantity_Product_IdAndDeleted(id, false)
        );
    }

    @Override
    public FeedbackSummary getFeedbackSummaryForStaff(UUID id) {
        return objectMapperUtil.mapFeedbackSummary(
                feedbackRepository.findByOrderDetail_ProductQuantity_Product_Id(id)
        );
    }

}
