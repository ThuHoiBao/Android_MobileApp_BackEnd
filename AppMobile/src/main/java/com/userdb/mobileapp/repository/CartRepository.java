package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Cart;
import com.userdb.mobileapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser_Id(long userId);
 ;
}
