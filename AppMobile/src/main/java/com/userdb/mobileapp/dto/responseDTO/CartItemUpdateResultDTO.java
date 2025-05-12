package com.userdb.mobileapp.dto.responseDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemUpdateResultDTO {
    private int cartItemId;
    private int updatedQuantity;
    private boolean outOfStock;
    private String message;
}
