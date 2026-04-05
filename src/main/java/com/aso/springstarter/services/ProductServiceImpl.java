package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.product.ProductRequest;
import com.aso.springstarter.dtos.product.ProductResponse;
import com.aso.springstarter.dtos.product.ProductStockRequest;
import com.aso.springstarter.entiies.ProductEntity;
import com.aso.springstarter.entiies.ProductStatus;
import com.aso.springstarter.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    // userRepository

    @Override
    public List<ProductResponse> getAllProducts() {
        final var products = productRepository.findAll();
        return products.stream()
            .map(ProductEntity::toDto)
            .toList();
    }

    @Override
    public ProductResponse getProduct(UUID productId) {
        final var product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return product.toDto();
    }

    @Override
    public ProductResponse getAvailableProduct(UUID productId) {
        return productRepository.findByIdAndStatus(productId, ProductStatus.AVAILABLE)
            .map(ProductEntity::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Override
    public Page<ProductResponse> getPaginatedProducts(Pageable pageable, List<ProductStatus> statuses) {
        return productRepository.findAllByStatusIn(pageable, statuses).map(ProductEntity::toDto);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        final var product = new ProductEntity(
            null,
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getStockQuantity(),
            ProductStatus.AVAILABLE,
            null
        );
        productRepository.save(product);
        return product.toDto();
    }

    @Override
    @Transactional
    public void updateProduct(UUID productId, ProductRequest request) {
        final var product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProductStock(UUID productId, ProductStockRequest request) {
        final var product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setStockQuantity(product.getStockQuantity() + request.getQuantity());
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }

}
