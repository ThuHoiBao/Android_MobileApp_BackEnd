package com.userdb.mobileapp.dto.responseDTO;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private int cartItemId;
    private String productName;
    private String color;
    private double price;
    private int quantity;
    private String productImage;
}
