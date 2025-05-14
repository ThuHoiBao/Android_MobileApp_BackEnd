package com.userdb.mobileapp.dto.responseDTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDTO {
    private String productName;
    private String productImage;
    private String color;
    private double price;
    private int quantity;
}
