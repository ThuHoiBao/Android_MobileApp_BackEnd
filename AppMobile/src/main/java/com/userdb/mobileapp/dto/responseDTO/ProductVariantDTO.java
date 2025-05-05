package com.userdb.mobileapp.dto.responseDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private String color;
    private double price;
    private String imageUrl;
    private int stock;
}