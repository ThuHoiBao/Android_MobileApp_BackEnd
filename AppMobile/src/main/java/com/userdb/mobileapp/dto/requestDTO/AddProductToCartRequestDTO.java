package com.userdb.mobileapp.dto.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToCartRequestDTO {
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("color")
    private String color;
    @JsonProperty("quantity")
    private int quantity;
}
