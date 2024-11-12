package com.fintech.database.repository;

import com.fintech.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByName(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByName(String username);
}