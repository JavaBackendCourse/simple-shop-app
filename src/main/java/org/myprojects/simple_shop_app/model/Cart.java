package org.myprojects.simple_shop_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @PrePersist
    public void prePersist() {
        totalPrice = BigDecimal.ZERO;
        items = new ArrayList<>();
    }

    @PreUpdate
    public void updateTotalPrice() {
        if (items != null || !items.isEmpty()) {
            totalPrice = items.stream().map(
                    item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            ).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            totalPrice = BigDecimal.ZERO;
        }
    }
}
