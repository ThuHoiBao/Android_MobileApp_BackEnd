package com.userdb.mobileapp.service;


import com.userdb.mobileapp.dto.responseDTO.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getAllProductsByCategory(int categoryId);

}
