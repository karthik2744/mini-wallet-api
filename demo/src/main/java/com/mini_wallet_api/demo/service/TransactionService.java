package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.entity.Transaction;
import com.mini_wallet_api.demo.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public void saveTransaction(
            Transaction transaction) {

        transactionRepository.save(transaction);
    }
}