package com.userdb.mobileapp.convert;

import com.userdb.mobileapp.dto.responseDTO.OrderItemResponseDTO;
import com.userdb.mobileapp.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class OrderItemConvert {

    public OrderItemResponseDTO convertOrderItemDTO(OrderItem orderItem) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();

        dto.setOrderId(orderItem.getOrder().getOrderId());
        dto.setOrderItemID(orderItem.getOrderItemID());
        dto.setProductName(orderItem.getProduct().getProductName());
        dto.setColor(orderItem.getProduct().getColor());
        dto.setOrderStatus(orderItem.getOrder().getOrderStatus().name());
        dto.setOrderDate(orderItem.getOrder().getOrderDate());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setAddress(orderItem.getOrder().getAddressDelivery().getAddress());// 💥 Quan trọng!
        dto.setFullName(orderItem.getOrder().getAddressDelivery().getFullName());
        dto.setPhoneNumber(orderItem.getOrder().getAddressDelivery().getPhoneNumber());
        // Gán ảnh nếu có
        if (orderItem.getProduct().getImageProducts() != null &&
                !orderItem.getProduct().getImageProducts().isEmpty()) {
            dto.setImageUrl("https://storage.googleapis.com/bucket_mobileapp/images/" +
                    orderItem.getProduct().getImageProducts().get(0).getImageProduct());
        }

        // Tính totalPrice
        double totalPrice = orderItem.getPrice() * orderItem.getQuantity();
        double roundedTotalPrice = new BigDecimal(totalPrice)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        dto.setTotalPrice(roundedTotalPrice);

        return dto;
    }

}
