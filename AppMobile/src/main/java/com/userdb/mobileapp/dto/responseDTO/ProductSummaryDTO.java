package com.userdb.mobileapp.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductSummaryDTO {
    private String productName;
    private List<ProductVariantDTO> variants;
    private int soldCount;
}
