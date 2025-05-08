package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.*;
import com.userdb.mobileapp.entity.Cart;
import com.userdb.mobileapp.entity.CartItem;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.service.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartServiceImpl cartService;
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addProductToCart(@RequestBody AddProductToCartRequestDTO request, @RequestParam("userId") long userId)  {
        try {
            Cart cart = cartService.addProductToCart(userId, request);
            return ResponseEntity.ok().body(ResponseObject.builder().status(HttpStatus.OK)
                    .data(CartResponseDTO.fromCart(request, cart, userId))
                    .message("Add Cart Successfully!")
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null).message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<List<CartItemUpdateResponseDTO>> updateCartItems(
            @RequestParam("userId") long userId,
            @RequestBody List<CartItemUpdateRequestDTO> updates) throws DataNotFoundException {

        List<CartItem> cartItemList = cartService.updateProductInCart(updates, userId);

        // Map request DTOs to response DTOs (assuming updated values are same as request)
        List<CartItemUpdateResponseDTO> responseDTOs = updates.stream()
                .map(req -> new CartItemUpdateResponseDTO(
                        req.getProductName(),
                        req.getColor(),
                        req.getNewQuantity()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/items")
    public List<CartItemDTO> getCartItems( @RequestParam long userId) throws DataNotFoundException {
        // Xử lý token (có thể sử dụng JWT để xác thực)
//        String token = authHeader.replace("Bearer ", "");
//        System.out.println("Received token: " + token);  // In ra token để kiểm tra
//        System.out.println("Received userId: " + userId);  // In ra userId để kiểm tra

        // Lấy dữ liệu giỏ hàng từ service
        List<CartItemDTO> cartItems = cartService.getCartItemsByUserId(userId);
        return cartItems;
    }

    @DeleteMapping("/item")
    public ResponseEntity<String> removeCartItem(@RequestParam long userId, @RequestParam int cartItemId) {
        try {
            cartService.removeCartItem(userId, cartItemId);
            return ResponseEntity.ok("Cart item deleted successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/update-cart")
    public ResponseEntity<?> updateCart( @RequestParam Long userId) throws DataNotFoundException {
        try {
            List<CartItemOutOfStock> list = cartService.updateCartItem(userId);
            return ResponseEntity.ok(list);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
