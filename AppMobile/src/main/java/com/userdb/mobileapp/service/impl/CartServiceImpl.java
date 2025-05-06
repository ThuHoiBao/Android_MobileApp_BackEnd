package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.dto.requestDTO.AddProductToCartRequestDTO;
import com.userdb.mobileapp.dto.requestDTO.AddressDeliveryDTO;
import com.userdb.mobileapp.dto.requestDTO.CartItemUpdateRequestDTO;
import com.userdb.mobileapp.entity.*;
import com.userdb.mobileapp.exception.DataNotFoundException;
import com.userdb.mobileapp.repository.CartItemRepository;
import com.userdb.mobileapp.repository.CartRepository;
import com.userdb.mobileapp.repository.ProductRepository;
import com.userdb.mobileapp.repository.UserRepository;
import com.userdb.mobileapp.service.ICart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICart {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Cart addProductToCart(long userId, AddProductToCartRequestDTO request) throws DataNotFoundException {

        Product product = productRepository
                .findTopByProductNameAndColor(request.getProductName(), request.getColor())
                .orElseThrow(() -> new DataNotFoundException("Product is not found"));

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(userRepository.findById(userId).orElseThrow());
                    return cartRepository.save(newCart);
                });

        CartItem existingItem = cart.getCardItems().stream()
                .filter(item -> item.getProduct().getProductName().equals(product.getProductName()) &&
                        item.getProduct().getColor().equals(product.getColor()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    @Override
    public List<CartItem> updateProductInCart(List<CartItemUpdateRequestDTO> updates, long userId) throws DataNotFoundException {
            List<CartItem> cartItemList = new ArrayList<>();
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new DataNotFoundException("Cart not found"));
        for (CartItemUpdateRequestDTO update : updates) {
            Product product = productRepository
                    .findTopByProductNameAndColor(update.getProductName(), update.getColor())
                    .orElseThrow(() -> new DataNotFoundException("Product is not found"));

            CartItem existingItem = cart.getCardItems().stream()
                    .filter(item -> item.getProduct().getProductName().equals(product.getProductName()) &&
                            item.getProduct().getColor().equals(product.getColor()))
                    .findFirst()
                    .orElse(null);
            if (existingItem != null) {
                cartItemList.add(existingItem);
                if(update.getNewQuantity() <= 0){
                    cartItemRepository.delete(existingItem);
                } else {
                    existingItem.setQuantity(update.getNewQuantity());
                    cartItemRepository.save(existingItem);
                }
                } else {
                throw new DataNotFoundException("Product is not found in the cart");
            }
        }
        return cartItemList;
    }
}
