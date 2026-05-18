package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.entity.transaction;
import com.mini_wallet_api.demo.repository.transactionrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class transactionservice {

    @Autowired
    private transactionrepository transactionRepository;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void saveTransaction(
            transaction transaction) {

        transactionRepository.save(transaction);
    }
}