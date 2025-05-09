package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.AddressDeliveryResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.CreateOrderResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.ResponseObject;
import com.userdb.mobileapp.entity.AddressDelivery;
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
    public ResponseEntity<ResponseObject> createOrderFromCart(@RequestBody CreateOrderRequestDTO request) {
        try {
            orderService.createOrderFromCart(request);
            return ResponseEntity.ok().body(ResponseObject.builder().status(HttpStatus.OK)
                    .data(CreateOrderResponseDTO.fromCreateOrderRequestDTO(request))
                    .message("Create Order Successfully!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null).message(e.getMessage())
                    .build());
        }
    }
}
