package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemServiceService;
    // API để lấy tất cả orderItem của một khách hàng
    @GetMapping("api/orders/{customerId}")
    public List<OrderItemResponseDTO> getOrderItemsByCustomerId(@PathVariable int customerId) {
        return orderItemServiceService.findAllByOrderCustomerId(customerId);
    }


    @GetMapping("api/orders/{customerId}/{orderStatus}")
    public List<OrderItemResponseDTO> getOrderItemsByCustomerIdAndStatus(@PathVariable("customerId") int customerId,
                                                                         @PathVariable("orderStatus") String orderStatus) {
        return orderItemServiceService.getOrderItemsByCustomerIdAndStatus(customerId, orderStatus);
    }



}
