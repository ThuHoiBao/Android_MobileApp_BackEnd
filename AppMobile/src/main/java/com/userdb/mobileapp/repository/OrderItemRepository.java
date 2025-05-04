package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.entity.Order;
import com.userdb.mobileapp.entity.OrderItem;
import com.userdb.mobileapp.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findAllByOrderCustomerUserIDAndOrderOrderStatus(int customerId, OrderStatus orderStatus);
    List<OrderItem> findAllByOrderCustomerUserID(int customerId);
    @Query("SELECT oi FROM OrderItem oi " +
            "WHERE oi.order.orderId = :orderId " +
            "AND oi.product.productName = :productName " +
            "AND oi.product.color = :color")
    List<OrderItem> findByOrderIdAndProductNameAndColor(int orderId, String productName, String color);
}

