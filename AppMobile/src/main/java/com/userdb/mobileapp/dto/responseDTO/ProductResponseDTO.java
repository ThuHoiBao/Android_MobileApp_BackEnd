package com.userdb.mobileapp.dto.responseDTO;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductResponseDTO {

    private int productId;
    private String productName;
    private double price;
    private String color;
    private String description;
    private int soldQuantity;
    private int remainingQuantity;
    private List<String> imageProducts;
}
