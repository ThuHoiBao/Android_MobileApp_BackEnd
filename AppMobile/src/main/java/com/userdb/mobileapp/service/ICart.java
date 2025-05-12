package com.userdb.mobileapp.service;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemDTO;
import com.userdb.mobileapp.dto.responseDTO.CartItemUpdateResultDTO;
import com.userdb.mobileapp.entity.Cart;
import com.userdb.mobileapp.entity.CartItem;
import com.userdb.mobileapp.exception.DataNotFoundException;

import java.util.List;

public interface ICart {
    Cart addProductToCart(long userId, AddProductToCartRequestDTO request) throws DataNotFoundException;
    List<CartItemUpdateResultDTO> updateProductInCart(List<CartItemUpdateRequestDTO> updates) throws DataNotFoundException;
    List<CartItemDTO> getCartItemsByUserId(long userId) throws DataNotFoundException;
    void removeCartItem(int cartItemId) throws DataNotFoundException;
    CartItem updateProductCartItem(int cartItemId, CartItemUpdateRequestDTO updates) throws DataNotFoundException;
}
