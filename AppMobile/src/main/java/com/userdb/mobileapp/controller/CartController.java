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

    @PutMapping("/update/cartItem")
    public ResponseEntity<CartItemDTO> updateCartItemId(@RequestParam int cartItemId,@RequestBody CartItemUpdateRequestDTO cartItemUpdateRequestDTO){
        try{
            CartItem cartItem = cartService.updateProductCartItem(cartItemId, cartItemUpdateRequestDTO);

        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @PutMapping("/update")
    public ResponseEntity<List<CartItemUpdateResultDTO>> updateCartItems(
            @RequestBody List<CartItemUpdateRequestDTO> updates) throws DataNotFoundException {
        List<CartItemUpdateResultDTO> cartItemList = cartService.updateProductInCart(updates);
        return ResponseEntity.ok(cartItemList);
    }

    @DeleteMapping("/item")
    public ResponseEntity<String> removeCartItem(@RequestParam int cartItemId) {
        try {
            cartService.removeCartItem(cartItemId);
            return ResponseEntity.ok("Cart item deleted successfully");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/items")
    public ResponseEntity<?> getCartItems( @RequestParam long userId) throws DataNotFoundException {
        // Xử lý token (có thể sử dụng JWT để xác thực)
//        String token = authHeader.replace("Bearer ", "");
//        System.out.println("Received token: " + token);  // In ra token để kiểm tra
//        System.out.println("Received userId: " + userId);  // In ra userId để kiểm tra

        // Lấy dữ liệu giỏ hàng từ service
        try {
            List<CartItemDTO> cartItems = cartService.getCartItemsByUserId(userId);
            return ResponseEntity.ok(cartItems);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
