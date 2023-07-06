package com.sparta.spartalevel1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.spartalevel1.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
