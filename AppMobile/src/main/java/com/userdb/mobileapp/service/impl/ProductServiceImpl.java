package com.userdb.mobileapp.service.impl;
//
//
import com.userdb.mobileapp.convert.ProductConvert;
import com.userdb.mobileapp.dto.responseDTO.ProductResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.ProductSummaryDTO;
import com.userdb.mobileapp.dto.responseDTO.ProductVariantDTO;
import com.userdb.mobileapp.entity.ImageProduct;
import com.userdb.mobileapp.entity.Product;
import com.userdb.mobileapp.repository.ProductRepository;
import com.userdb.mobileapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductConvert productConvert;


    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDTO> productsResponseDTOs = new ArrayList<>();
        Map<String, Product> groupPhones = new HashMap<>();

        for (Product product : products) {
            if (!groupPhones.containsKey(product.getProductName())) {
                groupPhones.put(product.getProductName(), product);
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                productResponseDTO = productConvert.toProductResponseDTO(product);

                // Lấy danh sách ảnh từ Google Cloud Storage và trả về URL
                List<String> imageUrls = new ArrayList<>();
                for (ImageProduct imageProduct : product.getImageProducts()) {
                    String imageUrl = "https://storage.googleapis.com/bucket_mobileapp/images/" + imageProduct.getImageProduct();
                    imageUrls.add(imageUrl);
                }
                productResponseDTO.setImageProducts(imageUrls);

                productResponseDTO.setSoldQuantity(productRepository.countByProductNameAndStatusFalse(product.getProductName()));
                productResponseDTO.setRemainingQuantity(productRepository.countByProductNameAndStatusTrue(product.getProductName()));

                productsResponseDTOs.add(productResponseDTO);
            }
        }

        // Sắp xếp theo số lượng bán (soldQuantity)
        productsResponseDTOs.sort((dto1, dto2) -> Integer.compare(dto2.getSoldQuantity(), dto1.getSoldQuantity()));
        return productsResponseDTOs;
    }

    @Override
    public List<ProductResponseDTO> getAllProductsByCategory(int categoryId) {
        List<Product> products = productRepository.findByCategoryCategoryID(categoryId);
        List<ProductResponseDTO> productsResponseDTOs = new ArrayList<>();
        Map<String, Product> groupPhones = new HashMap<>();

        for (Product product : products) {
            if (!groupPhones.containsKey(product.getProductName())) {
                groupPhones.put(product.getProductName(), product);
                ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                productResponseDTO = productConvert.toProductResponseDTO(product);

                // Lấy danh sách ảnh từ Google Cloud Storage và trả về URL
                List<String> imageUrls = new ArrayList<>();
                for (ImageProduct imageProduct : product.getImageProducts()) {
                    String imageUrl = "https://storage.googleapis.com/bucket_mobileapp/images/" + imageProduct.getImageProduct();
                    imageUrls.add(imageUrl);
                }
                productResponseDTO.setImageProducts(imageUrls);

                productResponseDTO.setSoldQuantity(productRepository.countByProductNameAndStatusFalse(product.getProductName()));
                productResponseDTO.setRemainingQuantity(productRepository.countByProductNameAndStatusTrue(product.getProductName()));

                productsResponseDTOs.add(productResponseDTO);
            }
        }

        // Sắp xếp theo số lượng bán (soldQuantity)
        productsResponseDTOs.sort((dto1, dto2) -> Integer.compare(dto2.getSoldQuantity(), dto1.getSoldQuantity()));
        return productsResponseDTOs;
    }

    @Override
    public ProductSummaryDTO getProductSummary(String productName) {
        List<Product> products = productRepository.findAllByProductName(productName);

        if (products.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm với tên: " + productName);
        }

        // Tập hợp các biến thể: không trùng price + image
        List<ProductVariantDTO> variants = new ArrayList<>();
        Map<String, ProductVariantDTO> variantMap = new HashMap<>();

        for (Product p : products) {
            String color = p.getColor(); // Giả định Product có trường `color`
            if (color == null) continue;

            if (p.getImageProducts() != null && !p.getImageProducts().isEmpty()) {
                String imageUrl = p.getImageProducts().get(0).getImageProduct();

                // Nếu chưa có biến thể màu này, tạo mới
                if (!variantMap.containsKey(color)) {
                    variantMap.put(color, new ProductVariantDTO(color, p.getPrice(), imageUrl, 0));
                }

                // Nếu sản phẩm chưa bán (status = true), tăng số lượng tồn kho
                if (p.isStatus()) {
                    ProductVariantDTO dto = variantMap.get(color);
                    dto.setStock(dto.getStock() + 1);
                }
            }
        }
//        for (Map.Entry<String, ProductVariantDTO> entry : variantMap.entrySet()) {
//            String color = entry.getKey();
//            ProductVariantDTO variant = entry.getValue();
//
//            System.out.println("Color: " + color);
//            System.out.println("  Price: " + variant.getPrice());
//            System.out.println("  Image URL: " + variant.getImageUrl());
//            System.out.println("  Stock: " + variant.getStock());
//            System.out.println("------------------------------");
//        }

        // Đếm số sản phẩm đã bán
        int soldCount = (int) products.stream().filter(p -> !p.isStatus()).count();

        //Đếm sản phẩm chưa bán theo màu sắc

        return new ProductSummaryDTO(productName, new ArrayList<>(variantMap.values()), soldCount);
    }
}
