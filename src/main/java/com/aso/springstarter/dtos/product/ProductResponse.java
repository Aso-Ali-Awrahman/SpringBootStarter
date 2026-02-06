package com.aso.springstarter.dtos.product;

import java.util.UUID;

import lombok.Value;

@Value
public class ProductResponse {
    UUID id;
    String name;
    String description;
    Double price;
    Integer stockQuantity;
}
