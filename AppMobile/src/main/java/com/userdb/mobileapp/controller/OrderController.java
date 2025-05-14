package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.*;
import com.userdb.mobileapp.entity.AddressDelivery;
import com.userdb.mobileapp.entity.Order;
import com.userdb.mobileapp.repository.OrderRepository;
import com.userdb.mobileapp.service.OrderItemService;
import com.userdb.mobileapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/orders/from-cart")
    public ResponseEntity<OrderResponseDTO> createOrderFromCart(@RequestBody CreateOrderRequestDTO request) {
        try{
            Order order =  orderService.createOrderFromCart(request);
            OrderResponseDTO orderResponseDTO = OrderResponseDTO.fromOrder(order);
            return ResponseEntity.ok(orderResponseDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
