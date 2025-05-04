package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    // Tìm tất cả OrderItem của khách hàng dựa vào customerId
    List<OrderItemResponseDTO> findAllByOrderCustomerId(int customerId);

    List<OrderItemResponseDTO> getOrderItemsByCustomerIdAndStatus(int customerId,String orderStatus);

}
