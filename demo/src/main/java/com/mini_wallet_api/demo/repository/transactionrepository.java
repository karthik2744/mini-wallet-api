package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.entity.transaction;
import com.mini_wallet_api.demo.entity.wallet;

import com.mini_wallet_api.demo.enums.transactionstatus;
import com.mini_wallet_api.demo.enums.transactiontype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface transactionrepository
        extends JpaRepository<transaction, Long> {

    Optional<transaction>
    findByReferenceId(String referenceId);

    List<transaction>
    findByWallet(wallet wallet);

    List<transaction>
    findByWalletOrderByCreatedAtDesc(
            wallet wallet
    );
    List<transaction>
    findByWalletAndTransactionTypeOrderByCreatedAtDesc(
            wallet wallet,
            transactiontype transactionType
    );
    List<transaction>
    findByTransactionTypeOrderByCreatedAtDesc(
            transactiontype transactionType
    );
    List<transaction>
    findByStatusOrderByCreatedAtDesc(
            transactionstatus status
    );
    List<transaction>
    findByTransactionTypeAndStatusOrderByCreatedAtDesc(
            transactiontype type,
            transactionstatus status
    );
    List<transaction>
    findByWalletAndStatusOrderByCreatedAtDesc(
            wallet wallet,
            transactionstatus status
    );
    List<transaction>
    findByWalletAndTransactionTypeAndStatusOrderByCreatedAtDesc(
            wallet wallet,
            transactiontype type,
            transactionstatus status
    );
}