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
        // Fetch all products matching the product name
        List<Product> products = productRepository.findAllByProductName(productName);
        System.out.println("123: " + products.size());

        if (products.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm với tên: " + productName);
        }

        // Map to store the variants by color
        Map<String, ProductVariantDTO> variantMap = new HashMap<>();

        for (Product p : products) {
            System.out.println(p.getProductName() + " " + p.getColor() + " " + p.isStatus());
            String color = p.getColor(); // Assuming Product has a field `color`
            if (color == null) continue;

            if (p.getImageProducts() != null && !p.getImageProducts().isEmpty()) {
                String imageUrl = "https://storage.googleapis.com/bucket_mobileapp/images/" + p.getImageProducts().get(0).getImageProduct(); // Get the first image URL

                // Check if the color variant already exists in the map
                if (!variantMap.containsKey(color)) {
                    // If not, create a new variant DTO with the initial stock value
                    variantMap.put(color, new ProductVariantDTO(color, p.getPrice(), imageUrl, 0));
                }

                // Get the existing ProductVariantDTO for this color
                ProductVariantDTO dto = variantMap.get(color);

                // If the product is available (status = true), increment stock
                if (p.isStatus()) {
                    dto.setStock(dto.getStock() + 1);
                }
            }
        }

        // Count how many products have been sold (status = false)
        int soldCount = (int) products.stream().filter(p -> !p.isStatus()).count();

        // Prepare the list of all variants
        List<ProductVariantDTO> variantList = new ArrayList<>(variantMap.values());

        // Return the product summary with variants and sold count
        return new ProductSummaryDTO(productName, variantList, soldCount);
    }
}
