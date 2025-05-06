package com.userdb.mobileapp.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.entity.Cart;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {
    @JsonProperty("cart_id")
    private int cartID;

    @JsonProperty("user_id")
    private Long userID;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("color")
    private String color;

    @JsonProperty("quantity")
    private int quantity;

    public static CartResponseDTO fromCart(AddProductToCartRequestDTO addProductToCartRequestDTO, Cart cart, Long userId) {
        return CartResponseDTO.builder()
                .cartID(cart.getCartID())
                .userID(userId)
                .productName(addProductToCartRequestDTO.getProductName())
                .color(addProductToCartRequestDTO.getColor())
                .quantity(addProductToCartRequestDTO.getQuantity())
                .build();
    }

}
