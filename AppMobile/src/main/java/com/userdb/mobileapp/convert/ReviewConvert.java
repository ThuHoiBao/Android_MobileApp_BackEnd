package com.userdb.mobileapp.convert;

import com.userdb.mobileapp.dto.requestDTO.ReviewRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.ProductResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.ReviewResponseDTO;
import com.userdb.mobileapp.entity.ImageReview;
import com.userdb.mobileapp.entity.Product;
import com.userdb.mobileapp.entity.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewConvert {
    @Autowired
    ModelMapper modelMapper;
    public Review toResponseDTOReview(ReviewRequestDTO reviewRequestDTO) {
        Review phoneResponseDTO = modelMapper.map(reviewRequestDTO, Review.class);
        return phoneResponseDTO;
    }
    public ReviewResponseDTO toReviewResponseDTO(Review review) {
        ReviewResponseDTO reviewResponseDTO = modelMapper.map(review, ReviewResponseDTO.class);
        List<String> images = new ArrayList<>();
        for (ImageReview imageReview : review.getImageReviews()) {

            images.add("https://storage.googleapis.com/bucket_mobileapp/images/"+"review_" + imageReview.getId() + "_"+imageReview.getImageReview());
        }
        // Gán danh sách hình ảnh vào ReviewResponseDTO
        reviewResponseDTO.setImages(images);
        return reviewResponseDTO;
    }

}

