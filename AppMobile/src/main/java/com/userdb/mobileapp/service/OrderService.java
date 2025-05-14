package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;
import com.userdb.mobileapp.entity.Order;
import com.userdb.mobileapp.exception.DataNotFoundException;

public interface OrderService {
    void cancelOrder(int orderId);
    Order createOrderFromCart(CreateOrderRequestDTO request) throws DataNotFoundException;

    Order  updateOrderStatus(int orderId, String vnp_Amount,String vnp_BankCode, String vnp_PayDate) throws DataNotFoundException;
    List<OrderItemDTO> loadOrderId(int orderId);
}
