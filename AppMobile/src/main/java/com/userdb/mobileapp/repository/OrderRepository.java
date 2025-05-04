package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findById(int orderId);
}
