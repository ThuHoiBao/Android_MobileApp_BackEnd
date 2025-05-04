package com.userdb.mobileapp.entity;

import com.userdb.mobileapp.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemID;

    @ManyToOne
    @JoinColumn(name = "orderID")
    private Order order;

    @OneToOne(mappedBy = "orderItem")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;

    private Double price;

    private int quantity;
}
