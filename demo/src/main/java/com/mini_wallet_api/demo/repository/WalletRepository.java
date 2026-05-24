package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.entity.Wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletRepository
        extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByMsisdn(String msisdn);
    @Query("""
SELECT COALESCE(SUM(w.balance), 0)
FROM Wallet w
""")
    BigDecimal getTotalWalletBalance();
}