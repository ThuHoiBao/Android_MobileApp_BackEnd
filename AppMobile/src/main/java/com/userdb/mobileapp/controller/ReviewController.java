package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.requestDTO.ReviewRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.ReviewResponseDTO;
import com.userdb.mobileapp.entity.ImageReview;
import com.userdb.mobileapp.entity.Review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userdb.mobileapp.service.ReviewService;
import com.userdb.mobileapp.service.impl.GoogleCloudStorageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    //private final ReviewService reviewService;
    @Autowired
    private GoogleCloudStorageServiceImpl googleCloudStorageService;

    // Google Cloud Storage Service
    @PostMapping(value = "/review", consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadReview(
            @RequestPart("review") String reviewJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        try {
            // Convert JSON String to DTO
            ObjectMapper objectMapper = new ObjectMapper();
            ReviewRequestDTO reviewDTO = objectMapper.readValue(reviewJson, ReviewRequestDTO.class);
            reviewService.saveReview(reviewDTO,images);
            return ResponseEntity.ok("Đánh giá đã được lưu");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi xử lý đánh giá: " + e.getMessage());
        }
    }@GetMapping("/reviews/{orderItemId}")
    public ResponseEntity<Boolean> checkReviewExists(@PathVariable int orderItemId) {
        // Kiểm tra xem OrderItem có tồn tại trong bảng Review không
        boolean exists = reviewService.checkReviewExists(orderItemId);  // Call service method to check
        return ResponseEntity.ok(exists);
    }


    // API trả về Review theo orderItemId
    @GetMapping("/review/{orderItemId}")
    public ResponseEntity<ReviewResponseDTO> getReviewByOrderItemId(@PathVariable int orderItemId) {

        ReviewResponseDTO reviewDTO = reviewService.getReviewByOrderItemId(orderItemId);

          if (reviewDTO != null) {
            return ResponseEntity.ok(reviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
