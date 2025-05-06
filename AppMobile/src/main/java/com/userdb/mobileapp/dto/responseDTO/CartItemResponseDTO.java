package com.userdb.mobileapp.dto.responseDTO;

import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemResponseDTO {
    private String productName;
    private String color;
    private int newQuantity;

    public static List<CartItemResponseDTO> fromCart(List<CartItemUpdateRequestDTO> updates, long userId) {
        return updates.stream().map(update ->
                new CartItemResponseDTO(
                        update.getProductName(),
                        update.getColor(),
                        update.getNewQuantity()
                )
        ).toList();
    }

}
