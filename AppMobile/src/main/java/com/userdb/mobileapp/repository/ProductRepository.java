package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Product;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    int countByProductNameAndStatusFalse(String productName);
    // Đếm số lượng điện thoại với status = 1 (chưa bán)
    int countByProductNameAndStatusTrue(String productName);

    // Tìm tất cả điện thoại thuộc một danh mục cụ thể
    List<Product> findByCategoryCategoryID(int categoryID);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) = LOWER(:productName)")
    List<Product> findAllByProductName(@Param("productName") String productName);

    Optional<Product> findTopByProductNameAndColor(String productName, String color);
    List<Product> findByProductNameAndColorAndStatusTrue(String productName, String color);

    @Query("SELECT p FROM Product p WHERE p.productName = :productName AND p.color = :color AND p.productId <> :productId AND status = true")
    List<Product> findTopSimilarProducts(
            @Param("productName") String productName,
            @Param("color") String color,
            @Param("productId") int productId,
            Pageable pageable
    );



}
