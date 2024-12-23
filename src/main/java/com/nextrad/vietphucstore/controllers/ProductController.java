package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.product.CreateProduct;
import com.nextrad.vietphucstore.dtos.responses.product.ProductDetail;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiListItemResponse;
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

    @GetMapping("/product")
    public ResponseEntity<ApiItemResponse<ProductDetail>> getProduct(@RequestParam UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(productService.getProduct(id), null));
    }

}
