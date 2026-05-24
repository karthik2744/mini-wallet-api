package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobileNumber(String mobileNumber);
    long count();

}
