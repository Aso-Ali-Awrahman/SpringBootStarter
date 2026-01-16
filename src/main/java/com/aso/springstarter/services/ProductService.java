package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.ProductRequest;
import com.aso.springstarter.dtos.ProductResponse;
import com.aso.springstarter.dtos.ProductStockRequest;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(UUID productId);

    ProductResponse createProduct(ProductRequest request);

    void updateProduct(UUID productId, ProductRequest request);

    void updateProductStock(UUID productId, ProductStockRequest request);

    void deleteProduct(UUID productId);

}
