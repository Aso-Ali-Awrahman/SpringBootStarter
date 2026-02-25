package com.aso.springstarter.controllers;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aso.springstarter.IntegrationTestBase;
import com.aso.springstarter.dtos.product.ProductRequest;
import com.aso.springstarter.dtos.product.ProductResponse;
import com.aso.springstarter.dtos.product.ProductStockRequest;
import com.aso.springstarter.entiies.ProductStatus;
import com.aso.springstarter.services.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

public class ProductControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private static final String PRODUCT_NAME = "Shoe";
    private static final String PRODUCT_DESCRIPTION = "Shoe description";
    private static final Double PRODUCT_PRICE = 100.0;
    private static final Integer PRODUCT_STOCK_QUANTITY = 10;

    @Test
    void shouldGetAllProducts(SoftAssertions softly) throws Exception {
        // given
        createProduct();

        // when
        final var result = mockMvc.perform(get("/protected/products"))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<ProductResponse>>() {});
        softly.assertThat(response).size().isEqualTo(1);
        softly.assertThat(response.getFirst().getName()).isEqualTo(PRODUCT_NAME);
        softly.assertThat(response.getFirst().getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        softly.assertThat(response.getFirst().getPrice()).isEqualTo(PRODUCT_PRICE);
        softly.assertThat(response.getFirst().getStockQuantity()).isEqualTo(PRODUCT_STOCK_QUANTITY);
        softly.assertThat(response.getFirst().getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    void shouldGetProduct(SoftAssertions softly) throws Exception {
        // given
        final var productId = createProduct();

        // when
        final var result = mockMvc.perform(get("/protected/products/{productId}", productId))
            .andExpect(status().isOk())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class);
        softly.assertThat(response.getId()).isEqualTo(productId);
        softly.assertThat(response.getName()).isEqualTo(PRODUCT_NAME);
        softly.assertThat(response.getDescription()).isEqualTo(PRODUCT_DESCRIPTION);
        softly.assertThat(response.getPrice()).isEqualTo(PRODUCT_PRICE);
        softly.assertThat(response.getStockQuantity()).isEqualTo(PRODUCT_STOCK_QUANTITY);
        softly.assertThat(response.getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    void shouldReturnProductNotFound(SoftAssertions softly) throws Exception {
        // given
        final var productId = UUID.randomUUID();

        // when
        final var result = mockMvc.perform(get("/protected/products/" + productId))
            .andExpect(status().isNotFound())
            .andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Product not found");
    }

    @Test
    void shouldCreateProduct(SoftAssertions softly) throws Exception {
        // given
        final var request = new ProductRequest("Keyboard", "Keyboard description", 100.0, 10);

        // when
        final var result = mockMvc.perform(
                post("/protected/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk()).andReturn();

        // then
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProductResponse.class);
        softly.assertThat(response.getName()).isEqualTo(request.getName());
        softly.assertThat(response.getDescription()).isEqualTo(request.getDescription());
        softly.assertThat(response.getPrice()).isEqualTo(request.getPrice());
        softly.assertThat(response.getStockQuantity()).isEqualTo(request.getStockQuantity());
        softly.assertThat(response.getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    void shouldUpdateProduct(SoftAssertions softly) throws Exception {
        // given
        final var productId = createProduct();
        final var request = new ProductRequest("Keyboard", "Silver style", 50.0, 5);

        // when
        mockMvc.perform(
                put("/protected/products/" + productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNoContent());

        // then
        final var updatedProduct = productService.getProduct(productId);
        softly.assertThat(updatedProduct.getName()).isEqualTo(request.getName());
        softly.assertThat(updatedProduct.getDescription()).isEqualTo(request.getDescription());
        softly.assertThat(updatedProduct.getPrice()).isEqualTo(request.getPrice());
        softly.assertThat(updatedProduct.getStockQuantity()).isEqualTo(request.getStockQuantity());
    }

    @Test
    void shouldNotUpdateProductDueNotFound(SoftAssertions softly) throws Exception {
        // given
        final var productId = UUID.randomUUID();
        final var request = new ProductRequest("Keyboard", "Silver style", 50.0, 5);

        // when
        final var result = mockMvc.perform(
                put("/protected/products/" + productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNotFound())
            .andReturn();

        //
        final var response = objectMapper.readValue(result.getResponse().getContentAsString(), ProblemDetail.class);
        softly.assertThat(response.getDetail()).isEqualTo("Product not found");
    }

    @Test
    void shouldUpdateProductStockQuantity(SoftAssertions softly) throws Exception {
        // given
        final var productId = createProduct();
        final var request = new ProductStockRequest(5);

        // when
        mockMvc.perform(
                patch("/protected/products/" + productId + "/stock")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isNoContent());

        // then
        final var updatedProduct = productService.getProduct(productId);
        softly.assertThat(updatedProduct.getStockQuantity()).isEqualTo(request.getQuantity()+PRODUCT_STOCK_QUANTITY);
    }

    @Test
    void shouldDeleteProduct(SoftAssertions softly) throws Exception {
        // given
        final var productId = createProduct();

        // when
        mockMvc.perform(delete("/protected/products/{productId}", productId))
            .andExpect(status().isNoContent());

        // then
        softly.assertThatThrownBy(() -> productService.getProduct(productId))
            .isInstanceOf(ResponseStatusException.class);
    }

    private UUID createProduct() {
        return productService.createProduct(new ProductRequest(
            PRODUCT_NAME, PRODUCT_DESCRIPTION, PRODUCT_PRICE, PRODUCT_STOCK_QUANTITY
        )).getId();
    }

}
