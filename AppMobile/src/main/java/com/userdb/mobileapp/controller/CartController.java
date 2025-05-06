package com.userdb.mobileapp.controller;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.CartResponseDTO;
import com.userdb.mobileapp.dto.responseDTO.ResponseObject;
import com.userdb.mobileapp.entity.Cart;
import com.userdb.mobileapp.entity.CartItem;
import com.userdb.mobileapp.entity.User;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.service.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ResponseObject> updateCartItems(
            @RequestParam("userId") long userId,
            @RequestBody List<CartItemUpdateRequestDTO> updates) {
        User user = new User();
        user.setId(userId);
        // Call the service to update multiple CartItems
        try {
            List<CartItem> cartItemList = cartService.updateProductInCart(updates, userId);

            // Chỉnh sửa: Gọi đúng phương thức `fromCart()`
            return ResponseEntity.ok().body(ResponseObject.builder()
                    .status(HttpStatus.OK)
                    .data(CartItemResponseDTO.fromCart(updates, userId))  // Đảm bảo gọi đúng phương thức `fromCart()`
                    .message("Update Cart Successfully!")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .message(e.getMessage())
                    .build());
        }
    }
}
