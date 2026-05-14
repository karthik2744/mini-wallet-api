// userrepository.java

package com.mini_wallet_api.demo.repository;

import com.mini_wallet_api.demo.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userrepository
        extends JpaRepository<user, Long> {

    Optional<user> findByMsisdn(String msisdn);
}