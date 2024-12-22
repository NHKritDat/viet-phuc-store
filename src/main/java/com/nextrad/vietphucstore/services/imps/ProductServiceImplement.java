package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.responses.product.SearchProduct;
import com.nextrad.vietphucstore.entities.product.Product;
import com.nextrad.vietphucstore.enums.product.ProductStatus;
import com.nextrad.vietphucstore.repositories.product.*;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
        Page<Product> products = productRepository.findByNameContainsIgnoreCaseAndStatusNotAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                search,
                ProductStatus.REMOVED,
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
        Page<Product> products = productRepository.findByNameContainsIgnoreCaseAndProductType_NameInAndProductCollection_NameInAndProductQuantities_ProductSize_NameIn(
                search,
                Arrays.asList(types),
                Arrays.asList(collections),
                Arrays.asList(sizes),
                pageableUtil.getPageable(Product.class, request)
        );
        return products.map(this::convertProductToSearchProduct);
    }

    private SearchProduct convertProductToSearchProduct(Product product) {
        return new SearchProduct(product.getName(), product.getUnitPrice());
    }

}
