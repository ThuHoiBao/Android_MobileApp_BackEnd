package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Product;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    int countByProductNameAndStatusFalse(String productName);
    // Đếm số lượng điện thoại với status = 1 (chưa bán)
    int countByProductNameAndStatusTrue(String productName);

    // Tìm tất cả điện thoại thuộc một danh mục cụ thể
    List<Product> findByCategoryCategoryID(int categoryID);

    @Query("SELECT p FROM Product p WHERE p.productName = :productName")
    List<Product> findAllByProductName(@Param("productName") String productName);

}
