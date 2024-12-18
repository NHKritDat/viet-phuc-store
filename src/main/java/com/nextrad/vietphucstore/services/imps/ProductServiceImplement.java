package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.repositories.product.ProductCollectionRepository;
import com.nextrad.vietphucstore.repositories.product.ProductRepository;
import com.nextrad.vietphucstore.repositories.product.ProductSizeRepository;
import com.nextrad.vietphucstore.repositories.product.ProductTypeRepository;
import com.nextrad.vietphucstore.services.ProductService;
import com.nextrad.vietphucstore.utils.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final ProductCollectionRepository productCollectionRepository;
    private final ProductSizeRepository productSizeRepository;
    private final PageableUtil pageableUtil;
}
