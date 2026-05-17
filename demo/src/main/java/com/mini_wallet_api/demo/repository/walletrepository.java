package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.entity.wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface walletrepository
        extends JpaRepository<wallet, Long> {

    Optional<wallet> findByMsisdn(String msisdn);
}