package com.aso.springstarter.configurations;

import com.aso.springstarter.repositories.ProductRepository;
import com.aso.springstarter.services.ProductService;
import com.aso.springstarter.services.ProductServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    ProductService productService(ProductRepository productRepository){
        return new ProductServiceImpl(productRepository);
    }

}
