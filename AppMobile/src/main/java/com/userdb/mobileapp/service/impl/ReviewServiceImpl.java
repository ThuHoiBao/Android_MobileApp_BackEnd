package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.convert.ReviewConvert;
import com.userdb.mobileapp.dto.requestDTO.ReviewRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.ReviewDTO;
import com.userdb.mobileapp.dto.responseDTO.ReviewResponseDTO;
import com.userdb.mobileapp.entity.ImageReview;
import com.userdb.mobileapp.entity.Order;
import com.userdb.mobileapp.entity.OrderItem;
import com.userdb.mobileapp.entity.Review;
import com.userdb.mobileapp.enums.OrderStatus;
import com.userdb.mobileapp.repository.*;
import com.userdb.mobileapp.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private GoogleCloudStorageServiceImpl googleCloudStorageService; //
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ImageReviewRepository imageReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewConvert reviewConvert;
    // Google Cloud Storage Service
    @Override
    public void saveReview(ReviewRequestDTO reviewRequestDTO, List<MultipartFile> images) {
        // Tìm các OrderItems khớp với thông tin trong reviewRequestDTO
        List<OrderItem> matchingOrderItems = orderItemRepository.findByOrderIdAndProductNameAndColor(
                reviewRequestDTO.getOrderId(),
                reviewRequestDTO.getProductName(),
                reviewRequestDTO.getColor()
        );

        if (matchingOrderItems.isEmpty()) {
            log.warn("No matching OrderItem found for the given orderId, productName, and color.");
            return;
        }

        // Lưu tất cả Review cho từng OrderItem
        for (OrderItem orderItem : matchingOrderItems) {
            // Tạo một Review mới cho từng OrderItem
            Review review = new Review();
            review.setRatingValue(reviewRequestDTO.getRatingValue());
            review.setDate(reviewRequestDTO.getDate());
            review.setComment(reviewRequestDTO.getComment());

            // Gán Review cho OrderItem
            orderItem.setReview(review);
            review.setOrderItem(orderItem); // Gán lại OrderItem cho Review

            // Gán các ảnh cho Review
            List<ImageReview> imageReviews = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile image : images) {
                    String originalName = image.getOriginalFilename();
                    ImageReview imageReview = new ImageReview();
                    imageReview.setReview(review); // Liên kết ảnh với Review
                    imageReview.setImageReview(originalName); // chỉ lưu tên gốc
                    imageReviews.add(imageReview);
                }
                review.setImageReviews(imageReviews); // Gán tất cả ảnh vào Review
            }

            // Lưu Review vào DB (Cascade sẽ tự động lưu ImageReview)
            reviewRepository.save(review);

            // Upload ảnh lên Google Cloud Storage sau khi đã lưu Review và ImageReview
            for (int i = 0; i < imageReviews.size(); i++) {
                ImageReview imageReview = imageReviews.get(i);
                MultipartFile image = images.get(i);
                String originalName = image.getOriginalFilename();
                String uniqueName = "review_" + imageReview.getId() + "_" + originalName;

                try {
                    googleCloudStorageService.uploadImageToGoogleCloudStorage(uniqueName, image);
                    log.info("Uploaded image: {} (original: {})", uniqueName, originalName);
                } catch (IOException e) {
                    log.error("Failed to upload image: {}", uniqueName, e);
                }
            }
        }

        // Cập nhật trạng thái của Order thành "FEEDBACKED"
        Optional<Order> optionalOrder = orderRepository.findById(reviewRequestDTO.getOrderId());  // Sử dụng findById từ OrderRepository

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(OrderStatus.FEEDBACKED); // Cập nhật trạng thái Order
            // Lưu lại Order sau khi cập nhật trạng thái
            orderRepository.save(order);  // Lưu lại đơn hàng bằng OrderRepository
            log.info("Order status updated to FEEDBACKED for orderId: {}", order.getOrderId());
        } else {
            log.warn("Order not found for orderId: {}", reviewRequestDTO.getOrderId());
        }
    }

    @Override
    public boolean checkReviewExists(int orderItemId) {
        Optional<Review> review = reviewRepository.findByOrderItem_OrderItemID(orderItemId);
        return review.isPresent();  // Trả về true nếu tồn tại, false nếu không tồn tại
    }

    @Override
    public ReviewResponseDTO getReviewByOrderItemId(int orderItemId) {
        Review review = reviewRepository.findByOrderItem_OrderItemID(orderItemId).orElse(null);
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO=reviewConvert.toReviewResponseDTO(review);
        return reviewResponseDTO;
    }

    @Override
    public List<ReviewDTO> getReviewsByProductName(String productName) {
        //Lay danh sach cac review dua tren productName
        List<Review> reviews = reviewRepository.findReviewsByProductName(productName);
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for(Review review : reviews){
            String userName = review.getOrderItem().getOrder().getUser().getFullName();
            LocalDate reviewDate = review.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Chuyển đổi LocalDate thành chuỗi với định dạng yyyy-MM-dd
            String formattedDate = reviewDate.toString(); // "yyyy-MM-dd"
            List<String> imageReviews = review.getImageReviews().stream()
                    .map(imageReview -> "https://storage.googleapis.com/bucket_mobileapp/images/" + imageReview.getImageReview())
                    .collect(Collectors.toList());
            // Tạo ReviewDTO và thêm vào danh sách
            reviewDTOs.add(new ReviewDTO(
                    userName,
                    formattedDate,
                    review.getRatingValue(),
                    review.getComment(),
                    imageReviews
            ));
        }
        return reviewDTOs;
    }
}
