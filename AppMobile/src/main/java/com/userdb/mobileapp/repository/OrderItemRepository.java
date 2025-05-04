package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.OrderItem;
import com.userdb.mobileapp.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findAllByOrderUserIdAndOrderOrderStatus(int customerId, OrderStatus orderStatus);
    List<OrderItem> findAllByOrderUserId(int customerId);
    @Query("SELECT oi FROM OrderItem oi " +
            "WHERE oi.order.orderId = :orderId " +
            "AND oi.product.productName = :productName " +
            "AND oi.product.color = :color")
    List<OrderItem> findByOrderIdAndProductNameAndColor(int orderId, String productName, String color);
}

