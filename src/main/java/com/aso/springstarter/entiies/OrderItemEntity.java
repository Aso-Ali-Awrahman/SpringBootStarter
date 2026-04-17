package com.aso.springstarter.entiies;

import java.time.Instant;
import java.util.UUID;

import com.aso.springstarter.dtos.order.OrderWithItemsResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "store", name = "order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    public OrderWithItemsResponse.OrderItemResponse toDto() {
        return new OrderWithItemsResponse.OrderItemResponse(
            id,
            product.getName(),
            quantity,
            price,
            createdAt
        );
    }

    public boolean checkQuantityBeforeCompletingOrder() {
        return product.getStockQuantity() < quantity;
    }

}
