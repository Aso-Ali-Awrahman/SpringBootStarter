package com.aso.springstarter.services;

import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.ProductRequest;
import com.aso.springstarter.dtos.ProductResponse;
import com.aso.springstarter.dtos.ProductStockRequest;
import lombok.AllArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class ProductServiceTest {

    private static final UUID PRODUCT_ID_1 = UUID.fromString("80db159b-963f-4071-9b15-9b963f0071e5");
    private static final UUID PRODUCT_ID_2 = UUID.fromString("81d2159b-963f-4071-9b15-9b963f0071e6");

    @Test
    void shouldGetAllProducts(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(false, 0);

        // when
        final var products = productService.getAllProducts();

        // then
        softly.assertThat(products).isNotEmpty();
        softly.assertThat(products.size()).isEqualTo(2);
        softly.assertThat(products.get(0).getId()).isEqualTo(PRODUCT_ID_1);
        softly.assertThat(products.get(1).getId()).isEqualTo(PRODUCT_ID_2);
    }

    @Test
    void shouldReturnEmptyListOfProducts(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(true, 0);

        // when
        final var products = productService.getAllProducts();

        // then
        softly.assertThat(products).isEmpty();
    }

    @Test
    void shouldGetProductById(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(false, 0);

        // when
        final var product = productService.getProduct(PRODUCT_ID_1);

        // then
        softly.assertThat(product).isNotNull();
        softly.assertThat(product.getId()).isEqualTo(PRODUCT_ID_1);
        softly.assertThat(product.getName()).isEqualTo("Product 1");
        softly.assertThat(product.getDescription()).isEqualTo("Description 1");
        softly.assertThat(product.getPrice()).isEqualTo(100.0);
    }

    @Test
    void shouldReturnNullWhenProductNotFound(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(true, 0);

        // when
        final var product = productService.getProduct(UUID.randomUUID());

        // then
        softly.assertThat(product).isNull();
    }

    @Test
    void shouldCreateProduct(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(false, 0);
        final var request = new ProductRequest("P1", "D1", 100.0, 50);

        // when
        final var product = productService.createProduct(request);

        // then
        softly.assertThat(product).isNotNull();
        softly.assertThat(product.getId()).isEqualTo(PRODUCT_ID_1);
        softly.assertThat(product.getName()).isEqualTo(request.getName());
        softly.assertThat(product.getDescription()).isEqualTo(request.getDescription());
        softly.assertThat(product.getPrice()).isEqualTo(100.0);
        softly.assertThat(product.getStockQuantity()).isEqualTo(request.getStockQuantity());
    }

    @Test
    void shouldUpdateProductById(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(false, 0);
        final var request = new ProductRequest("P2", "D1", 100.0, 50);

        // when
        productService.updateProduct(PRODUCT_ID_1, request);

        // then
        softly.assertThat(productService.count).isEqualTo(1);
    }

    @Test
    void shouldNotUpdateProductDueNotFound(SoftAssertions soflty) {
        // given
        final var productService = new TestProductService(true, 0);
        final var request = new ProductRequest("P2", "D1", 100.0, 50);

        // when
        productService.updateProduct(PRODUCT_ID_1, request);

        // then
        soflty.assertThat(productService.count).isEqualTo(0);
    }

    @Test
    void shouldUpdateProductStockById(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(false, 0);
        final var request = new ProductStockRequest(50);

        // when
        productService.updateProductStock(PRODUCT_ID_1, request);

        // then
        softly.assertThat(productService.count).isEqualTo(1);
    }

    @Test
    void shouldNotUpdateProductStockDueNotFound(SoftAssertions soflty) {
        // given
        final var productService = new TestProductService(true, 0);
        final var request = new ProductStockRequest(50);

        // when
        productService.updateProductStock(PRODUCT_ID_1, request);

        // then
        soflty.assertThat(productService.count).isEqualTo(0);
    }

    @Test
    void shouldDeleteProductById(SoftAssertions softly) {
        // given
        final var productService = new TestProductService(false, 0);

        // when
        productService.deleteProduct(PRODUCT_ID_1);

        // then
        softly.assertThat(productService.count).isEqualTo(1);
    }

    @AllArgsConstructor
    static final class TestProductService implements ProductService {

        private final boolean isFailed;
        private int count;

        @Override
        public List<ProductResponse> getAllProducts() {
            if (isFailed) {
                return List.of();
            }
            return List.of(
                new ProductResponse(
                    PRODUCT_ID_1, "Product 1", "Description 1", 100.0, 100
                ),
                new ProductResponse(
                    PRODUCT_ID_2, "Product 2", "Description 2", 200.0, 200
                )
            );
        }

        @Override
        public ProductResponse getProduct(UUID productId) {
            if (isFailed) {
                return null;
            }
            return new ProductResponse(
                PRODUCT_ID_1, "Product 1", "Description 1", 100.0, 100
            );
        }

        @Override
        public ProductResponse createProduct(ProductRequest request) {
            if (isFailed) {
                return null;
            }
            return new ProductResponse(
                PRODUCT_ID_1, request.getName(), request.getDescription(), request.getPrice(), request.getStockQuantity()
            );
        }

        @Override
        public void updateProduct(UUID productId, ProductRequest request) {
            if (isFailed) {
                return;
            }
            count++;
        }

        @Override
        public void updateProductStock(UUID productId, ProductStockRequest request) {
            if (isFailed) {
                return;
            }
            count++;
        }

        @Override
        public void deleteProduct(UUID productId) {
            if (isFailed) {
                return;
            }
            count++;
        }
    }

}
