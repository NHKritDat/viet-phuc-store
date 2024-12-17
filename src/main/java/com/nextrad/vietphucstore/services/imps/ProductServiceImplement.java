package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.repositories.ProductRepository;
import com.nextrad.vietphucstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
}
