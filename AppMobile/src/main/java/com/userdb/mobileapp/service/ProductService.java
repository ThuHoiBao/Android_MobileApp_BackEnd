package com.userdb.mobileapp.service;


import com.userdb.mobileapp.dto.responseDTO.ProductResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.ProductSummaryDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getAllProductsByCategory(int categoryId);
    ProductSummaryDTO getProductSummary(String productName);
}
