package com.userdb.mobileapp.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemUpdateRequestDTO {
    @JsonProperty("cartItem_Id")
    private int cartItemId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("color")
    private String color;
    @JsonProperty("new_quantity")
    private int newQuantity;
}
