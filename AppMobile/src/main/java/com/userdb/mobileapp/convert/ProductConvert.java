package com.userdb.mobileapp.convert;

import com.userdb.mobileapp.dto.responseDTO.ProductResponseDTO;
import com.userdb.mobileapp.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConvert {
    @Autowired
    ModelMapper modelMapper;

    public ProductResponseDTO toProductResponseDTO(Product product) {
        ProductResponseDTO phoneResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
        return phoneResponseDTO;
    }
}
