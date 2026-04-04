package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.PagingRequest;
import com.aso.springstarter.dtos.product.ProductRequest;
import com.aso.springstarter.dtos.product.ProductResponse;
import com.aso.springstarter.dtos.product.ProductStockRequest;
import com.aso.springstarter.entiies.ProductStatus;
import com.aso.springstarter.security.authorization.CustomerRoleRequired;
import com.aso.springstarter.security.authorization.DataEntryRoleRequired;
import com.aso.springstarter.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product Controller")
@RestController
@RequestMapping()
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = "/protected/products/all")
    @Operation(summary = "Get all products")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/protected/backoffice/products")
    @Operation(summary = "Get products using pagination")
    @ResponseStatus(HttpStatus.OK)
    @DataEntryRoleRequired
    public Page<ProductResponse> getProductsByPaginationBackoffice(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                                                   @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                                                   @RequestParam(name = "direction", defaultValue = "asc") String direction,
                                                                   @RequestParam(name = "status", defaultValue = "AVAILABLE") List<ProductStatus> statuses) {
        final var allowedSorts = List.of("id", "name", "stock_quantity");
        final var pagingRequest = new PagingRequest(page, size, sortBy, direction);

        return productService.getPaginatedProducts(pagingRequest.toPageable(allowedSorts), statuses);
    }

    @GetMapping(value = "/protected/products")
    @Operation(summary = "Get products using pagination")
    @ResponseStatus(HttpStatus.OK)
    @CustomerRoleRequired
    public Page<ProductResponse> getProductsByPaginationCustomer(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestParam(name = "sort", defaultValue = "id") String sortBy,
                                                                 @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        final var allowedSorts = List.of("id", "name");
        final var pagingRequest = new PagingRequest(page, size, sortBy, direction);

        return productService.getPaginatedProducts(pagingRequest.toPageable(allowedSorts), List.of(ProductStatus.AVAILABLE));
    }

    @GetMapping(value = "/protected/backoffice/products/{productId}")
    @Operation(summary = "Get product based on ID")
    @ResponseStatus(HttpStatus.OK)
//    @DataEntryRoleRequired
    public ResponseEntity<ProductResponse> getProductByDataEntry(@PathVariable UUID productId) {
        final var product = productService.getProduct(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = "/protected/products/{productId}")
    @Operation(summary = "Get product based on ID - Customer")
    @ResponseStatus(HttpStatus.OK)
//    @CustomerRoleRequired
    public ProductResponse getProductByCustomer(@PathVariable UUID productId) {
        return productService.getAvailableProduct(productId);
    }

    @PostMapping("/protected/backoffice/products")
    @ResponseStatus(HttpStatus.OK)
    @DataEntryRoleRequired
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/protected/backoffice/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DataEntryRoleRequired
    public ResponseEntity<Void> updateProduct(@PathVariable UUID productId, @Valid @RequestBody ProductRequest request) {
        productService.updateProduct(productId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/protected/backoffice/products/{productId}/stock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DataEntryRoleRequired
    public ResponseEntity<Void> updateProductStock(@PathVariable UUID productId, @Valid @RequestBody ProductStockRequest request) {
        productService.updateProductStock(productId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/protected/backoffice/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DataEntryRoleRequired
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }


}
