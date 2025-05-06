package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;

public interface OrderService {
    void cancelOrder(int orderId);
    void createOrderFromCart(CreateOrderRequestDTO request);
}
