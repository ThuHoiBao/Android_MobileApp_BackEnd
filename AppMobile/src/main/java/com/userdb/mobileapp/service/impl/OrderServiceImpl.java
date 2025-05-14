package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.dto.requestDTO.CreateOrderRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.OrderItemDTO;
import com.userdb.mobileapp.entity.*;
import com.userdb.mobileapp.enums.OrderStatus;
import com.userdb.mobileapp.enums.PaymentMethod;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.repository.*;
import com.userdb.mobileapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void cancelOrder(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        // Cập nhật trạng thái đơn hàng thành CANCELLED
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


    @Override
    public Order createOrderFromCart(CreateOrderRequestDTO request) throws DataNotFoundException {
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
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());

        order = orderRepository.save(order);

        // Tạo các order item từ cart item
        List<CartItem> cartItems = cartItemRepository.findAllById(request.getCartItemIds());

        for (CartItem cartItem : cartItems) {
            Product currentProduct = productRepository.findById(cartItem.getProduct().getProductId()).orElseThrow(() -> new DataNotFoundException("Product not found"));;
            List<Product> productList = productRepository.findTopSimilarProducts(
                    currentProduct.getProductName(),
                    currentProduct.getColor(),
                    currentProduct.getProductId(),
                    PageRequest.of(0, cartItem.getQuantity())
            );

            for (int i = 0; i < productList.size(); i++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                Product product = new Product();
                product.setProductId(productList.get(i).getProductId());
                orderItem.setProduct(product);
                orderItem.setQuantity(cartItem.getQuantity()); // mỗi dòng 1 sản phẩm
                orderItem.setPrice(cartItem.getProduct().getPrice());
                orderItemRepository.save(orderItem);
            }

            // Xóa khỏi giỏ hàng sau khi chuyển
            cartItemRepository.delete(cartItem);
        }
        return order;
    }

    @Override
    public Order updateOrderStatus(int orderId, String vnp_Amount,String vnp_BankCode, String vnp_PayDate) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setOrderStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        Payment payment = Payment.builder()
                        .amount(Long.parseLong(vnp_Amount))
                        .paymentDate(vnp_PayDate)
                        .paymentMethod(PaymentMethod.VNPAY)
                        .status(true)
                        .order(order)
                        .momoBillId(vnp_BankCode).build();

        paymentRepository.save(payment);


        List<OrderItem> orderItemList = orderItemRepository.findAllByOrderOrderId(orderId);
        if(!orderItemList.isEmpty()){
            for (OrderItem orderItem : orderItemList){
                Product product = productRepository.findById(orderItem.getProduct().getProductId()).orElseThrow(() -> new DataNotFoundException("Product Id is not found"));
                product.setStatus(false);
                productRepository.save(product);
            }
        }

        return order;
    }

    @Override
    public List<OrderItemDTO> loadOrderId(int orderId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderOrderId(orderId);
        Map<String, Integer> productMap = new HashMap<>();
//dedee
        for(OrderItem orderItem : orderItems){
            Product product = orderItem.getProduct();
            String productName = product.getProductName();
            String color = product.getColor();
            double price = product.getPrice();
            String productImage =  product.getImageProducts().isEmpty() ? null : "https://storage.googleapis.com/bucket_mobileapp/images/" + product.getImageProducts().get(0).getImageProduct();

            String key = productName + " - " + color + " - " + price + " - " + productImage;
            productMap.put(key, productMap.getOrDefault(key, 0) + 1);
        }

        return productMap.entrySet().stream()
                .map(entry -> {
                    String[] keyParts = entry.getKey().split(" - ");
                    String productName = keyParts[0];
                    String color = keyParts[1];
                    double price = Double.parseDouble(keyParts[2]);
                    String productImage = keyParts[3];
                    int count = entry.getValue();
                    return new OrderItemDTO(productName,productImage , color, price, count);
                }).collect(Collectors.toList());
    }
}
