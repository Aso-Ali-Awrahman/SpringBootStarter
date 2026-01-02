package com.aso.springstarter.controllers;

import java.util.List;

import com.aso.springstarter.entiies.ProductEntity;
import com.aso.springstarter.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/api/products")
    public List<ProductEntity> getProducts() {
        return productService.getAllProducts();
    }

    // Get product by id


}
