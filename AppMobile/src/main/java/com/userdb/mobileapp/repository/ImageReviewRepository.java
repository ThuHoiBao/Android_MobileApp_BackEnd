package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Category;
import com.userdb.mobileapp.entity.ImageReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageReviewRepository extends JpaRepository<ImageReview, Long> {
}
