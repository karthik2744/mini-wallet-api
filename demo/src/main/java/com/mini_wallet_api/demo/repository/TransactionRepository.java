package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.dto.TransactionAnalyticsResponse;
import com.mini_wallet_api.demo.entity.Transaction;
import com.mini_wallet_api.demo.entity.Wallet;

import com.mini_wallet_api.demo.enums.TransactionStatus;
import com.mini_wallet_api.demo.enums.TransactionType;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Optional<Transaction>
    findByReferenceId(
            String referenceId
    );

    // ADMIN FILTERS

    List<Transaction>
    findByTypeOrderByCreatedAtDesc(
            TransactionType type
    );

    List<Transaction>
    findByStatusOrderByCreatedAtDesc(
            TransactionStatus status
    );

    List<Transaction>
    findByTypeAndStatusOrderByCreatedAtDesc(

            TransactionType type,

            TransactionStatus status
    );

    // USER FILTERS

    List<Transaction>
    findByWalletOrderByCreatedAtDesc(
            Wallet wallet
    );

    List<Transaction>
    findByWalletAndTypeOrderByCreatedAtDesc(

            Wallet wallet,

            TransactionType type
    );

    List<Transaction>
    findByWalletAndStatusOrderByCreatedAtDesc(

            Wallet wallet,

            TransactionStatus status
    );

    List<Transaction>
    findByWalletAndTypeAndStatusOrderByCreatedAtDesc(

            Wallet wallet,

            TransactionType type,

            TransactionStatus status
    );

    // COUNTS

    long countByTypeAndStatus(

            TransactionType type,

            TransactionStatus status
    );

    long countByStatus(
            TransactionStatus status
    );

    Page<Transaction>
    findByWallet_MsisdnContainingAndTypeAndStatus(

            String msisdn,

            TransactionType type,

            TransactionStatus status,

            Pageable pageable
    );

    Page<Transaction>
    findByWallet_MsisdnContaining(

            String msisdn,

            Pageable pageable
    );

    Page<Transaction>
    findByWallet_MsisdnContainingAndType(

            String msisdn,

            TransactionType type,

            Pageable pageable
    );

    Page<Transaction>
    findByWallet_MsisdnContainingAndStatus(

            String msisdn,

            TransactionStatus status,

            Pageable pageable
    );

    @Query("""

SELECT new com.mini_wallet_api.demo.dto.TransactionAnalyticsResponse(

    FUNCTION('DATE', t.createdAt),

    COUNT(t),

    COALESCE(SUM(
        CASE WHEN t.type =
        com.mini_wallet_api.demo.enums.TransactionType.CREDIT

        AND t.status =
        com.mini_wallet_api.demo.enums.TransactionStatus.SUCCESS

        THEN t.amount ELSE 0 END
    ), 0),

    COALESCE(SUM(
        CASE WHEN t.type =
        com.mini_wallet_api.demo.enums.TransactionType.DEBIT

        AND t.status =
        com.mini_wallet_api.demo.enums.TransactionStatus.SUCCESS

        THEN t.amount ELSE 0 END
    ), 0)

)

FROM Transaction t

GROUP BY FUNCTION('DATE', t.createdAt)

ORDER BY FUNCTION('DATE', t.createdAt)

""")

    List<TransactionAnalyticsResponse>
    getTransactionAnalytics();
}


