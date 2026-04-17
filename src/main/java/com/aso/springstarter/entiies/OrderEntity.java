package com.aso.springstarter.entiies;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.aso.springstarter.dtos.order.OrderResponse;
import com.aso.springstarter.dtos.order.OrderWithItemsResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "store", name = "order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItemEntity> orderItems;

    public boolean isNotAvailable() {
        return status == OrderStatus.COMPLETED || status == OrderStatus.EXPIRED;
    }

    public void completeOrder(Double totalPrice) {
        this.status = OrderStatus.COMPLETED;
        this.totalPrice = totalPrice;
        this.updatedAt = Instant.now();
    }

    public OrderResponse toDto() {
        return new OrderResponse(id, status, totalPrice, createdAt);
    }

    public OrderWithItemsResponse toDtoWithItems() {
        return new OrderWithItemsResponse(
            id,
            status,
            totalPrice,
            createdAt,
            orderItems.stream()
                .map(OrderItemEntity::toDto)
                .toList()
        );
    }

}
