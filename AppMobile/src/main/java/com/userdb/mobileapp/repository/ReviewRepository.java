package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Product;
import com.userdb.mobileapp.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository  extends JpaRepository<Review, Integer> {
    Optional<Review> findByOrderItem_OrderItemID(int orderItemId);
}
