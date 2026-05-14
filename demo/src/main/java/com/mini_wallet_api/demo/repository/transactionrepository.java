// transactionrepository.java

package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.entity.transaction;
import com.mini_wallet_api.demo.entity.wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface transactionrepository
        extends JpaRepository<transaction, Long> {

    Optional<transaction> findByReferenceId(String referenceId);

    List<transaction> findByWallet(wallet wallet);
}