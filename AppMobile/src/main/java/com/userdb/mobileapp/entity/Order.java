package com.userdb.mobileapp.entity;


import com.userdb.mobileapp.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;


    @OneToOne(mappedBy = "order")
    private Payment payment;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_delivery_id", referencedColumnName = "id")
    private AddressDelivery addressDelivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Date orderDate;
}
