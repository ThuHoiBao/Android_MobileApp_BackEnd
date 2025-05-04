package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.convert.OrderItemConvert;
import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.entity.Order;
import com.userdb.mobileapp.entity.OrderItem;
import com.userdb.mobileapp.enums.OrderStatus;
import com.userdb.mobileapp.repository.OrderItemRepository;
import com.userdb.mobileapp.repository.OrderRepository;
import com.userdb.mobileapp.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemConvert orderItemConvert;

    @Autowired
    OrderRepository orderRepository;

    public List<OrderItemResponseDTO> findAllByOrderCustomerId(int customerId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderUserId(customerId);
        orderItems.sort((o1, o2) -> o2.getOrder().getOrderDate().compareTo(o1.getOrder().getOrderDate()));

        Map<String, List<OrderItem>> groupedOrderItems = orderItems.stream()
                .collect(Collectors.groupingBy(oi -> oi.getOrder().getOrderId() + "-" +
                        oi.getProduct().getProductName() + "-" +
                        oi.getProduct().getColor() + "-" +
                        oi.getProduct().getSize()
                ));

        List<OrderItemResponseDTO> responseList = new ArrayList<>();

        List<List<OrderItem>> sortedGroups = groupedOrderItems.values().stream()
                .sorted((g1, g2) -> {
                    Date d1 = g1.get(0).getOrder().getOrderDate();
                    Date d2 = g2.get(0).getOrder().getOrderDate();
                    return d2.compareTo(d1); // Giảm dần
                })
                .collect(Collectors.toList());

        // Chuyển về DTO
        for (List<OrderItem> group : sortedGroups) {
            OrderItem first = group.get(0); // Lấy 1 phần tử đại diện
            OrderItemResponseDTO dto = orderItemConvert.convertOrderItemDTO(first);
            responseList.add(dto);
        }

        return responseList;
    }


    @Override
    public List<OrderItemResponseDTO> getOrderItemsByCustomerIdAndStatus(int customerId, String orderStatus) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderUserIdAndOrderOrderStatus(customerId,
                OrderStatus.valueOf(orderStatus));

        Map<String, List<OrderItem>> groupedOrderItems = orderItems.stream()
                .collect(Collectors.groupingBy(oi -> oi.getOrder().getOrderId() + "-" +
                        oi.getProduct().getProductName() + "-" +
                        oi.getProduct().getColor() + "-" +
                        oi.getProduct().getSize()
                ));


        // Sắp xếp các group theo thời gian đặt hàng mới nhất
        List<List<OrderItem>> sortedGroups = groupedOrderItems.values().stream()
                .sorted((g1, g2) -> {
                    Date d1 = g1.get(0).getOrder().getOrderDate();
                    Date d2 = g2.get(0).getOrder().getOrderDate();
                    return d2.compareTo(d1); // Giảm dần
                })
                .collect(Collectors.toList());

        // Chuyển về DTO
        List<OrderItemResponseDTO> responseList = new ArrayList<>();
        for (List<OrderItem> group : sortedGroups) {
            OrderItem first = group.get(0); // Lấy 1 phần tử đại diện
            OrderItemResponseDTO dto = orderItemConvert.convertOrderItemDTO(first);
            responseList.add(dto);
        }

        return responseList;
    }


}
