package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    int countByProductNameAndStatusFalse(String productName);
    // Đếm số lượng điện thoại với status = 1 (chưa bán)
    int countByProductNameAndStatusTrue(String productName);

    // Tìm tất cả điện thoại thuộc một danh mục cụ thể
    List<Product> findByCategoryCategoryID(int categoryID);

}
