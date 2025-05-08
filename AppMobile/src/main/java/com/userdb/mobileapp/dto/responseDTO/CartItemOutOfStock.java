package com.userdb.mobileapp.dto.responseDTO;

import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartItemOutOfStock {
    private int cartItemId;
    private boolean outOfStock;
}
