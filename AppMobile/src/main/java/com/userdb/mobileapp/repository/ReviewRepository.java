package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Product;
import com.userdb.mobileapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review, Integer> {
    Optional<Review> findByOrderItem_OrderItemID(int orderItemId);

    @Query("SELECT r FROM Review r " +
            "JOIN r.orderItem oi " +
            "JOIN oi.product p " +
            "JOIN r.orderItem.order o " +
            "JOIN o.user u " +
            "WHERE p.productName = :productName")
    List<Review> findReviewsByProductName(@Param("productName") String productName);
}
