package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.product.ProductCollectionResponse;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiListItemResponse;
import com.nextrad.vietphucstore.entities.product.ProductSize;
import com.nextrad.vietphucstore.entities.product.ProductType;
import com.nextrad.vietphucstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiListItemResponse<SearchProduct>> getProducts(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "0", required = false) double minPrice,
            @RequestParam(defaultValue = "10000000", required = false) double maxPrice,
            @RequestParam(defaultValue = "", required = false) String[] sizes,
            @RequestParam(defaultValue = "", required = false) String[] types,
            @RequestParam(defaultValue = "", required = false) String[] collections,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<SearchProduct> response = productService.getProducts(
                search, minPrice, maxPrice,
                sizes, types, collections,
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách các sản phẩm hiện có"
        ));
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<SearchProduct>> getProductsForStaff(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "0", required = false) double minPrice,
            @RequestParam(defaultValue = "10000000", required = false) double maxPrice,
            @RequestParam(defaultValue = "", required = false) String[] sizes,
            @RequestParam(defaultValue = "", required = false) String[] types,
            @RequestParam(defaultValue = "", required = false) String[] collections,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<SearchProduct> response = productService.getProductsForStaff(
                search, minPrice, maxPrice,
                sizes, types, collections,
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách tất cả các sản phẩm"
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiItemResponse<ProductDetail>> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.getProduct(id),
                "Đây là thông tin chi tiết của sản phẩm"
        ));
    }

    @GetMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductDetail>> getProductForStaff(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.getProductForStaff(id),
                "Đây là thông tin chi tiết của sản phẩm"
        ));
    }

    @PostMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductDetail>> createProduct(@RequestBody ModifyProductRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.createProduct(request),
                "Sản phẩm đã được tạo thành công"
        ));
    }

    @PutMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductDetail>> updateProduct(
            @PathVariable UUID id,
            @RequestBody ModifyProductRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.updateProduct(id, request),
                "Sản phẩm đã được cập nhật thành công"
        ));
    }

    @DeleteMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> deleteProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.deleteProduct(id)));
    }

    @GetMapping("/sizes")
    public ResponseEntity<ApiListItemResponse<ProductSize>> getSizes(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductSize> response = productService.getSizes(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách các kích cỡ sản phẩm hiện có"
        ));
    }

    @GetMapping("/sizes/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<ProductSize>> getSizesForStaff(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductSize> response = productService.getSizesForStaff(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách tất cả các kích cỡ sản phẩm"
        ));
    }

    @GetMapping("/sizes/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductSize>> getProductSize(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.getSize(id),
                "Đây là thông tin chi tiết của kích cỡ sản phẩm"
        ));
    }

    @PostMapping("/sizes/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductSize>> createProductSize(@RequestBody ModifySizeRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.createProductSize(request),
                "Kích cỡ sản phẩm đã được tạo thành công"
        ));
    }

    @PutMapping("/sizes/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductSize>> updateProductSize(
            @PathVariable UUID id,
            @RequestBody ModifySizeRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.updateProductSize(id, request),
                "Kích cỡ sản phẩm đã được cập nhật thành công"
        ));
    }

    @DeleteMapping("/sizes/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> deleteProductSize(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.deleteProductSize(id)));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiListItemResponse<ProductType>> getProductTypes(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductType> response = productService.getProductTypes(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách các loại sản phẩm hiện có"
        ));
    }

    @GetMapping("/types/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<ProductType>> getProductTypesForStaff(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductType> response = productService.getProductTypesForStaff(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách tất cả các loại sản phẩm"
        ));
    }

    @GetMapping("/types/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductType>> getProductType(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.getProductType(id),
                "Đây là thông tin chi tiết của loại sản phẩm"
        ));
    }

    @PostMapping("/types/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductType>> createProductType(@RequestBody ModifyTypeRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.createProductType(request),
                "Loại sản phẩm đã được tạo thành công"
        ));
    }

    @PutMapping("/types/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductType>> updateProductType(
            @PathVariable UUID id,
            @RequestBody ModifyTypeRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.updateProductType(id, request),
                "Loại sản phẩm đã được cập nhật thành công"
        ));
    }

    @DeleteMapping("/types/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> deleteProductType(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.deleteProductType(id)));
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiListItemResponse<ProductCollectionResponse>> getCollections(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductCollectionResponse> response = productService.getProductCollections(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách các bộ sưu tập sản phẩm hiện có"
        ));
    }

    @GetMapping("/collections/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<ProductCollectionResponse>> getCollectionsForStaff(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductCollectionResponse> response = productService.getProductCollectionsForStaff(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách tất cả các bộ sưu tập sản phẩm"
        ));
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<ApiItemResponse<ProductCollectionResponse>> getCollection(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.getProductCollection(id),
                "Đây là thông tin chi tiết của bộ sưu tập sản phẩm"
        ));
    }

    @GetMapping("/collections/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductCollectionResponse>> getCollectionForStaff(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.getProductCollectionForStaff(id),
                "Đây là thông tin chi tiết của bộ sưu tập sản phẩm"
        ));
    }

    @PostMapping("/collections/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductCollectionResponse>> createCollection(@RequestBody ModifyCollectionRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.createProductCollection(request),
                "Bộ sưu tập sản phẩm đã được tạo thành công"
        ));
    }

    @PutMapping("/collections/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductCollectionResponse>> updateCollection(
            @PathVariable UUID id,
            @RequestBody ModifyCollectionRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                productService.updateProductCollection(id, request),
                "Bộ sưu tập sản phẩm đã được cập nhật thành công"
        ));
    }

    @DeleteMapping("/collections/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> deleteCollection(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.deleteProductCollection(id)));
    }

    @GetMapping("/{productId}/feedbacks")
    public ResponseEntity<ApiListItemResponse<FeedbackResponse>> getFeedbacks(
            @PathVariable UUID productId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<FeedbackResponse> response = productService.getFeedbacks(productId,
                new PageableRequest(page - 1, size, direction, properties));
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách các đánh giá của sản phẩm"
        ));
    }

    @GetMapping("/{productId}/feedbacks/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<FeedbackResponse>> getFeedbacksForStaff(
            @PathVariable UUID productId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<FeedbackResponse> response = productService.getFeedbacksForStaff(productId,
                new PageableRequest(page - 1, size, direction, properties));
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                "Đây là danh sách tất cả các đánh giá của sản phẩm"
        ));
    }

    @PutMapping("/{id}/reactive/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> reactiveProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.reactiveProduct(id)));
    }

    @PutMapping("/types/{id}/reactive/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> reactiveProductType(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.reactiveProductType(id)));
    }

    @PutMapping("/collections/{id}/reactive/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> reactiveProductCollection(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.reactiveProductCollection(id)));
    }

    @PutMapping("/sizes/{id}/reactive/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> reactiveProductSize(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.reactiveProductSize(id)));
    }
}
