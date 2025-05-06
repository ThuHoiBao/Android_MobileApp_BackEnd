package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;
import com.userdb.mobileapp.entity.*;
import com.userdb.mobileapp.enums.OrderStatus;
import com.userdb.mobileapp.repository.*;
import com.userdb.mobileapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressDeliveryRepository addressDeliveryRepository;

    @Override
    public void cancelOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // Cập nhật trạng thái đơn hàng thành CANCELLED
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


    @Override
    public void createOrderFromCart(CreateOrderRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tìm địa chỉ mặc định
        AddressDelivery defaultAddress = addressDeliveryRepository
                .findByUser_IdAndIsDefaultTrue(user.getId())
                .orElseThrow(() -> new RuntimeException("No default delivery address found for user"));

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setAddressDelivery(defaultAddress);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setOrderDate(new Date());

        order = orderRepository.save(order);

        // Tạo các order item từ cart item
        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItemRepository.save(orderItem);

            // Xóa khỏi giỏ hàng
            cartItemRepository.delete(cartItem);
        }
    }
}
