package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.dto.amountrequest;
import com.mini_wallet_api.demo.dto.transactionresponse;
import com.mini_wallet_api.demo.dto.walletresponse;
import com.mini_wallet_api.demo.entity.transaction;
import com.mini_wallet_api.demo.entity.wallet;
import com.mini_wallet_api.demo.enums.transactionstatus;
import com.mini_wallet_api.demo.enums.transactiontype;
import com.mini_wallet_api.demo.exception.customexception;
import com.mini_wallet_api.demo.repository.transactionrepository;
import com.mini_wallet_api.demo.repository.walletrepository;



import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j

public class walletservice {

    @Autowired
    private walletrepository walletRepository;

    @Autowired
    private transactionrepository transactionRepository;
    @Autowired
    private transactionservice transactionservice;


    // CREATE USER
    @Transactional
    public wallet createUser(String msisdn) {

        log.info("Creating user: {}", msisdn);

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

        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }


    // GET BALANCE
    @Transactional(readOnly = true)
    public walletresponse getBalance(String msisdn) {

        wallet wallet = getWallet(msisdn);

        return mapWallet(wallet);
    }


    // DEPOSIT
    @Transactional
    public walletresponse deposit(
            String msisdn,
            amountrequest request) {

        log.info(
                "Deposit request for {} amount {}",
                msisdn,
                request.getAmount()
        );

        wallet wallet = getWallet(msisdn);


        if (request.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            transaction failedTransaction =
                    new transaction();

            failedTransaction.setReferenceId(
                    generateReferenceId()
            );

            failedTransaction.setTransactionType(
                    transactiontype.CREDIT
            );

            failedTransaction.setAmount(
                    request.getAmount()
            );

            failedTransaction.setAvailableBalance(
                    wallet.getBalance()
            );

            failedTransaction.setStatus(
                    transactionstatus.FAILED
            );

            failedTransaction.setWallet(wallet);

            transactionservice.saveTransaction(
                    failedTransaction
            );

            log.warn(
                    "Failed deposit for {} due to invalid amount",
                    msisdn
            );

            throw new customexception(
                    "Amount must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }

        BigDecimal updatedBalance =
                wallet.getBalance()
                        .add(request.getAmount());

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);


        transaction transaction =
                new transaction();

        transaction.setReferenceId(
                generateReferenceId()
        );

        transaction.setTransactionType(
                transactiontype.CREDIT
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setAvailableBalance(
                updatedBalance
        );

        transaction.setStatus(
                transactionstatus.SUCCESS
        );

        transaction.setWallet(wallet);

        transactionRepository.save(transaction);

        return mapWallet(wallet);
    }


    // WITHDRAW
    @Transactional
    public walletresponse withdraw(
            String msisdn,
            amountrequest request) {

        log.info(
                "Withdraw request for {} amount {}",
                msisdn,
                request.getAmount()
        );

        wallet wallet = getWallet(msisdn);
        if (request.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            transaction failedTransaction =
                    new transaction();

            failedTransaction.setReferenceId(
                    generateReferenceId()
            );

            failedTransaction.setTransactionType(
                    transactiontype.DEBIT
            );

            failedTransaction.setAmount(
                    request.getAmount()
            );

            failedTransaction.setAvailableBalance(
                    wallet.getBalance()
            );

            failedTransaction.setStatus(
                    transactionstatus.FAILED
            );

            failedTransaction.setWallet(wallet);

            transactionservice.saveTransaction(
                    failedTransaction
            );

            log.warn(
                    "Failed withdraw for {} due to invalid amount",
                    msisdn
            );

            throw new customexception(
                    "Amount must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (wallet.getBalance()
                .compareTo(request.getAmount()) < 0) {

            transaction failedTransaction =
                    new transaction();

            failedTransaction.setReferenceId(
                    generateReferenceId()
            );

            failedTransaction.setTransactionType(
                    transactiontype.DEBIT
            );

            failedTransaction.setAmount(
                    request.getAmount()
            );

            failedTransaction.setAvailableBalance(
                    wallet.getBalance()
            );

            failedTransaction.setStatus(
                    transactionstatus.FAILED
            );

            failedTransaction.setWallet(wallet);

            transactionservice.saveTransaction(
                    failedTransaction
            );

            log.warn(
                    "Failed withdraw for {} due to insufficient balance",
                    msisdn
            );

            throw new customexception(
                    "Insufficient balance",
                    HttpStatus.CONFLICT
            );
        }

        BigDecimal updatedBalance =
                wallet.getBalance()
                        .subtract(request.getAmount());

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);


        transaction transaction =
                new transaction();

        transaction.setReferenceId(
                generateReferenceId()
        );

        transaction.setTransactionType(
                transactiontype.DEBIT
        );

        transaction.setAmount(
                request.getAmount()
        );

        transaction.setAvailableBalance(
                updatedBalance
        );

        transaction.setStatus(
                transactionstatus.SUCCESS
        );

        transaction.setWallet(wallet);

        transactionRepository.save(transaction);

        return mapWallet(wallet);
    }


    // GET ALL TRANSACTIONS
    @Transactional(readOnly = true)
    public List<transactionresponse>
    getTransactions(String msisdn){

        wallet wallet = getWallet(msisdn);

        return transactionRepository
                .findByWallet(wallet)
                .stream()
                .map(this::mapTransaction)
                .collect(Collectors.toList());
    }


    // GET SINGLE TRANSACTION
    @Transactional(readOnly = true)
    public transactionresponse
    getTransactionByReferenceId(
            String referenceId) {

        transaction transaction =
                transactionRepository
                        .findByReferenceId(referenceId)
                        .orElseThrow(() ->
                                new customexception(
                                        "Transaction not found",
                                        HttpStatus.NOT_FOUND
                                ));

        return mapTransaction(transaction);
    }


    // GET ALL USERS
    @Transactional(readOnly = true)
    public List<walletresponse>
    getAllUsers(int page, int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return walletRepository
                .findAll(pageable)
                .stream()
                .map(this::mapWallet)
                .collect(Collectors.toList());
    }

    // DELETE USER
    @Transactional
    public void deleteUser(String msisdn) {

        wallet wallet = getWallet(msisdn);

        transactionRepository.deleteAll(
                transactionRepository.findByWallet(wallet)
        );

        walletRepository.delete(wallet);

        log.info("Deleted user: {}", msisdn);
    }


    // GET WALLET HELPER
    private wallet getWallet(String msisdn) {

        return walletRepository
                .findByMsisdn(msisdn)
                .orElseThrow(() ->
                        new customexception(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));
    }


    // MAP WALLET RESPONSE
    private walletresponse mapWallet(
            wallet wallet) {

        walletresponse response =
                new walletresponse();

        response.setId(wallet.getId());

        response.setMsisdn(wallet.getMsisdn());

        response.setBalance(wallet.getBalance());

        response.setCreatedAt(
                wallet.getCreatedAt()
        );

        return response;
    }


    // MAP TRANSACTION RESPONSE
    private transactionresponse mapTransaction(
            transaction transaction) {

        transactionresponse response =
                new transactionresponse();

        response.setReferenceId(
                transaction.getReferenceId()
        );

        response.setTransactionType(
                transaction.getTransactionType()
                        .name()
        );

        response.setAmount(
                transaction.getAmount()
        );

        response.setAvailableBalance(
                transaction.getAvailableBalance()
        );

        response.setStatus(
                transaction.getStatus()
                        .name()
        );

        response.setCreatedAt(
                transaction.getCreatedAt()
        );

        return response;
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