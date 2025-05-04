package com.userdb.mobileapp.dto.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class OrderItemResponseDTO {
    private int orderItemID;
    private int orderId;
    private Double price;
    private int quantity;
    private String productName;
    private String color;
    private Double totalPrice;
    private String orderStatus;
    private String imageUrl;
    private Date orderDate;

}
