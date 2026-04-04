package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.product.ProductRequest;
import com.aso.springstarter.dtos.product.ProductResponse;
import com.aso.springstarter.dtos.product.ProductStockRequest;
import com.aso.springstarter.entiies.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(UUID productId);

    ProductResponse getAvailableProduct(UUID productId);

    Page<ProductResponse> getPaginatedProducts(Pageable pageable, List<ProductStatus> statuses);

    ProductResponse createProduct(ProductRequest request);

    void updateProduct(UUID productId, ProductRequest request);

    void updateProductStock(UUID productId, ProductStockRequest request);

    void deleteProduct(UUID productId);

}
