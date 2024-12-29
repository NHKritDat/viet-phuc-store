package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyCollectionRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyProductRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifySizeRequest;
import com.nextrad.vietphucstore.dtos.requests.product.ModifyTypeRequest;
import com.nextrad.vietphucstore.dtos.responses.order.FeedbackResponse;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiListItemResponse;
import com.nextrad.vietphucstore.entities.product.ProductCollection;
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
            @RequestParam(defaultValue = "", required = false) String[] sizes,
            @RequestParam(defaultValue = "", required = false) String[] types,
            @RequestParam(defaultValue = "", required = false) String[] collections,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<SearchProduct> response = productService.getProducts(search, sizes, types, collections,
                new PageableRequest(page - 1, size, direction, properties));
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                null
        ));
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<SearchProduct>> getProductsForStaff(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "", required = false) String[] sizes,
            @RequestParam(defaultValue = "", required = false) String[] types,
            @RequestParam(defaultValue = "", required = false) String[] collections,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<SearchProduct> response = productService.getProductsForStaff(search, sizes, types, collections,
                new PageableRequest(page - 1, size, direction, properties));
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                null
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiItemResponse<ProductDetail>> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.getProduct(id), null));
    }

    @PostMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductDetail>> createProduct(@RequestBody ModifyProductRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.createProduct(request), null));
    }

    @PutMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductDetail>> updateProduct(
            @PathVariable UUID id,
            @RequestBody ModifyProductRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.updateProduct(id, request), null));
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
                null
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
                null
        ));
    }

    @GetMapping("/sizes/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductSize>> getProductSize(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.getSize(id), null));
    }

    @PostMapping("/sizes/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductSize>> createProductSize(@RequestBody ModifySizeRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.createProductSize(request), null));
    }

    @PutMapping("/sizes/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductSize>> updateProductSize(
            @PathVariable UUID id,
            @RequestBody ModifySizeRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.updateProductSize(id, request), null));
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
                null
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
                null
        ));
    }

    @GetMapping("/types/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductType>> getProductType(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.getProductType(id), null));
    }

    @PostMapping("/types/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductType>> createProductType(@RequestBody ModifyTypeRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.createProductType(request), null));
    }

    @PutMapping("/types/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductType>> updateProductType(
            @PathVariable UUID id,
            @RequestBody ModifyTypeRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.updateProductType(id, request), null));
    }

    @DeleteMapping("/types/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<String>> deleteProductType(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, productService.deleteProductType(id)));
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiListItemResponse<ProductCollection>> getCollections(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductCollection> response = productService.getProductCollections(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                null
        ));
    }

    @GetMapping("/collections/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<ProductCollection>> getCollectionsForStaff(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<ProductCollection> response = productService.getProductCollectionsForStaff(
                new PageableRequest(page - 1, size, direction, properties)
        );
        return ResponseEntity.ok(new ApiListItemResponse<>(
                response.getContent(),
                response.getSize(),
                response.getNumber() + 1,
                response.getTotalElements(),
                response.getTotalPages(),
                null
        ));
    }

    @GetMapping("/collections/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductCollection>> getCollection(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.getProductCollection(id), null));
    }

    @PostMapping("/collections/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductCollection>> createCollection(@RequestBody ModifyCollectionRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.createProductCollection(request), null));
    }

    @PutMapping("/collections/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<ProductCollection>> updateCollection(
            @PathVariable UUID id,
            @RequestBody ModifyCollectionRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.updateProductCollection(id, request), null));
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
                null
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
                null
        ));
    }
}
