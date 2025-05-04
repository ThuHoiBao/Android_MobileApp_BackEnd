package com.userdb.mobileapp.repository;

import com.userdb.mobileapp.entity.Token;
import com.userdb.mobileapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);
    Optional<Token> findByToken(String token);
}
