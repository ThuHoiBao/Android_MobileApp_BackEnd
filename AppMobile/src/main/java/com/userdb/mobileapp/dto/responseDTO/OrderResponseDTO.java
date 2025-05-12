package com.userdb.mobileapp.dto.responseDTO;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.entity.*;
import com.userdb.mobileapp.enums.OrderStatus;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderResponseDTO {

    private int orderId;

    private Long userId;

    private Payment payment;

    private AddressDelivery addressDelivery;

    private String orderStatus;

    private Date orderDate;

    public static OrderResponseDTO fromOrder(Order order) {
        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getId())
                .orderStatus(order.getOrderStatus().toString())
                .orderDate(order.getOrderDate())
                .build();
    }
}
