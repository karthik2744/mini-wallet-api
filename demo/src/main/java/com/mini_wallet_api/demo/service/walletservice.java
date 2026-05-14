package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.dto.amountrequest;
import com.mini_wallet_api.demo.dto.transactionresponse;
import com.mini_wallet_api.demo.dto.walletresponse;
import com.mini_wallet_api.demo.entity.transaction;
import com.mini_wallet_api.demo.entity.wallet;
import com.mini_wallet_api.demo.exception.customexception;
import com.mini_wallet_api.demo.repository.transactionrepository;
import com.mini_wallet_api.demo.repository.walletrepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class walletservice {

    @Autowired
    private walletrepository walletRepository;

    @Autowired
    private transactionrepository transactionRepository;


    // CREATE USER
    public wallet createUser(String msisdn) {

        wallet existingWallet = walletRepository
                .findByMsisdn(msisdn)
                .orElse(null);

        if (existingWallet != null) {

            throw new customexception(
                    "User already exists",
                    HttpStatus.CONFLICT
            );
        }

        wallet wallet = new wallet();

        wallet.setMsisdn(msisdn);
        wallet.setBalance(0.0);
        wallet.setCreatedAt(LocalDateTime.now());

        return walletRepository.save(wallet);
    }


    // GET BALANCE
    public walletresponse getBalance(String msisdn) {

        wallet wallet = walletRepository
                .findByMsisdn(msisdn)
                .orElseThrow(() ->
                        new customexception(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));

        walletresponse response = new walletresponse();

        response.setMsisdn(wallet.getMsisdn());
        response.setBalance(wallet.getBalance());
        response.setId(wallet.getId());

        return response;
    }


    // DEPOSIT
    @Transactional
    public walletresponse deposit(String msisdn,
                                  amountrequest request) {

        wallet wallet = walletRepository
                .findByMsisdn(msisdn)
                .orElseThrow(() ->
                        new customexception(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));
        if (request.getAmount() <= 0) {

            throw new customexception(
                    "Amount must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }


        Double updatedBalance =
                wallet.getBalance() + request.getAmount();

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);


        // CREATE TRANSACTION
        transaction transaction = new transaction();

        transaction.setReferenceId(generateReferenceId());

        transaction.setTransactionType("CREDIT");

        transaction.setAmount(request.getAmount());

        transaction.setAvailableBalance(updatedBalance);

        transaction.setStatus("SUCCESS");

        transaction.setCreatedAt(LocalDateTime.now());

        transaction.setWallet(wallet);

        transactionRepository.save(transaction);


        walletresponse response =
                new walletresponse();

        response.setId(wallet.getId());

        response.setMsisdn(wallet.getMsisdn());

        response.setBalance(updatedBalance);

        response.setCreatedAt(
                wallet.getCreatedAt()
        );

        return response;
    }


    // WITHDRAW
    @Transactional
    public walletresponse withdraw(String msisdn,
                                   amountrequest request) {

        wallet wallet = walletRepository
                .findByMsisdn(msisdn)
                .orElseThrow(() ->
                        new customexception(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));
        if (request.getAmount() <= 0) {

            throw new customexception(
                    "Amount must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }


        if (wallet.getBalance() < request.getAmount()) {

            throw new customexception(
                    "Insufficient balance",
                    HttpStatus.CONFLICT
            );
        }


        Double updatedBalance =
                wallet.getBalance() - request.getAmount();

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);


        // CREATE TRANSACTION
        transaction transaction = new transaction();

        transaction.setReferenceId(generateReferenceId());

        transaction.setTransactionType("DEBIT");

        transaction.setAmount(request.getAmount());

        transaction.setAvailableBalance(updatedBalance);

        transaction.setStatus("SUCCESS");

        transaction.setCreatedAt(LocalDateTime.now());

        transaction.setWallet(wallet);

        transactionRepository.save(transaction);


        walletresponse response =
                new walletresponse();

        response.setId(wallet.getId());

        response.setMsisdn(wallet.getMsisdn());

        response.setBalance(updatedBalance);

        response.setCreatedAt(
                wallet.getCreatedAt()
        );

        return response;
    }


    // GET ALL TRANSACTIONS
    public List<transactionresponse>
    getTransactions(String msisdn) {

        wallet wallet = walletRepository
                .findByMsisdn(msisdn)
                .orElseThrow(() ->
                        new customexception(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));


        List<transaction> transactions =
                transactionRepository.findByWallet(wallet);


        return transactions.stream().map(t -> {

            transactionresponse response =
                    new transactionresponse();

            response.setReferenceId(
                    t.getReferenceId());

            response.setTransactionType(
                    t.getTransactionType());

            response.setAmount(
                    t.getAmount());

            response.setAvailableBalance(
                    t.getAvailableBalance());

            response.setStatus(
                    t.getStatus());

            response.setCreatedAt(
                    t.getCreatedAt());

            return response;

        }).collect(Collectors.toList());
    }


    // GET SINGLE TRANSACTION
    public transactionresponse
    getTransactionByReferenceId(String referenceId) {

        transaction transaction =
                transactionRepository
                        .findByReferenceId(referenceId)
                        .orElseThrow(() ->
                                new customexception(
                                        "Transaction not found",
                                        HttpStatus.NOT_FOUND
                                ));


        transactionresponse response =
                new transactionresponse();

        response.setReferenceId(
                transaction.getReferenceId());

        response.setTransactionType(
                transaction.getTransactionType());

        response.setAmount(
                transaction.getAmount());

        response.setAvailableBalance(
                transaction.getAvailableBalance());

        response.setStatus(
                transaction.getStatus());

        response.setCreatedAt(
                transaction.getCreatedAt());

        return response;
    }
    public List<walletresponse> getAllUsers() {

        List<wallet> wallets =
                walletRepository.findAll();

        return wallets.stream().map(wallet -> {

            walletresponse response =
                    new walletresponse();

            response.setId(wallet.getId());

            response.setMsisdn(wallet.getMsisdn());

            response.setBalance(wallet.getBalance());

            response.setCreatedAt(
                    wallet.getCreatedAt()
            );

            return response;

        }).toList();
    }

    // GENERATE REFERENCE ID
    private String generateReferenceId() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}