package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Cart;
import com.userdb.mobileapp.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByCartAndProduct_ProductIdAndProduct_Color(Cart cart, int productId, String color);

    List<CartItem> findByCart(Cart cart);
}
