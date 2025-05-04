package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.requestDTO.ReviewRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.ReviewResponseDTO;
import com.userdb.mobileapp.entity.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    void saveReview(ReviewRequestDTO reviewRequestDTO, List<MultipartFile> images);
    public boolean checkReviewExists(int orderItemId);
    ReviewResponseDTO getReviewByOrderItemId(int orderItemId);
}
