package com.aso.springstarter.services;

import java.util.List;

import com.aso.springstarter.entiies.ProductEntity;
import com.aso.springstarter.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

}
