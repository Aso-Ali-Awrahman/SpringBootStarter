package com.aso.springstarter.dtos;

import lombok.Value;

@Value
public class ProductRequest {
    String name;
    String description;
    Double price;
    Integer stockQuantity;
}
