package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Cart;
import com.userdb.mobileapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser_Id(long userId);
}
