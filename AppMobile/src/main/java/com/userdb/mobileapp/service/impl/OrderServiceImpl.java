package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.entity.Order;
import com.userdb.mobileapp.enums.OrderStatus;
import com.userdb.mobileapp.repository.OrderRepository;
import com.userdb.mobileapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public void cancelOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // Cập nhật trạng thái đơn hàng thành CANCELLED
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
