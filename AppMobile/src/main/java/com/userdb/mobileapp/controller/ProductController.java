package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.responseDTO.ProductResponseDTO;
import com.userdb.mobileapp.service.ProductService;
import com.userdb.mobileapp.service.impl.GoogleCloudStorageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ProductController {

    @Autowired
    private ProductService productService; // Giả sử bạn có ProductService để thao tác với dữ liệu

    @Autowired
    private GoogleCloudStorageServiceImpl googleCloudStorageService; // Google Cloud Storage Service

    // Lấy danh sách sản phẩm
    @GetMapping("/products")
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    // Upload ảnh và lưu URL vào database
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("productName") String productName) {
        String uniqueImageName1 = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        try {
            // Tạo tên ảnh duy nhất từ thời gian hiện tại
            String uniqueImageName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // Lưu ảnh lên Google Cloud Storage và nhận URL ảnh
            String imageUrl = googleCloudStorageService.uploadImageToGoogleCloudStorage(uniqueImageName, file);

            // Gọi service để lưu thông tin ảnh vào database (tên ảnh hoặc URL ảnh)
           // productService.saveImageUrl(imageUrl, productName);

            // Trả về URL của ảnh đã upload
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }

    @PostMapping("/uploadMedia")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                              @RequestParam("productName") String productName) {
        String uniqueImageName1 = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        try {
            // Tạo tên ảnh duy nhất từ thời gian hiện tại
            String uniqueImageName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // Lưu ảnh lên Google Cloud Storage và nhận URL ảnh
            String imageUrl = googleCloudStorageService.uploadImageToGoogleCloudStorage(uniqueImageName, file);

            // Gọi service để lưu thông tin ảnh vào database (tên ảnh hoặc URL ảnh)
            // productService.saveImageUrl(imageUrl, productName);

            // Trả về URL của ảnh đã upload
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file");
        }
    }
}
