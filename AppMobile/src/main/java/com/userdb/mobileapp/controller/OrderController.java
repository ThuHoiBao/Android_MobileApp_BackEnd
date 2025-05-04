package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.repository.OrderRepository;
import com.userdb.mobileapp.service.OrderItemService;
import com.userdb.mobileapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {


    @Autowired
    OrderService orderService;

    @PutMapping("api/orders/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Đơn hàng đã được huỷ");
    }
}
