package com.fintech.database.repository;

import com.fintech.database.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Boolean existsByJwt(String jwt);

    Token findByUserId(Long userId);
}