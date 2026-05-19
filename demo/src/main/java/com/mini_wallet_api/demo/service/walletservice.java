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

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class walletservice {

    private final walletrepository walletRepository;

    private final transactionrepository transactionRepository;

    private final transactionservice transactionservice;


    // CONSTRUCTOR INJECTION
    public walletservice(
            walletrepository walletRepository,
            transactionrepository transactionRepository,
            transactionservice transactionservice
    ) {

        this.walletRepository = walletRepository;

        this.transactionRepository =
                transactionRepository;

        this.transactionservice =
                transactionservice;
    }


    // CREATE USER
    @Transactional
    public wallet createUser(String msisdn) {

        log.info(
                "Creating user: {}",
                msisdn
        );

        wallet existingWallet =
                walletRepository
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
    public walletresponse getBalance(
            String msisdn
    ) {

        wallet wallet = getWallet(msisdn);

        return mapWallet(wallet);
    }


    // DEPOSIT
    @Transactional
    public walletresponse deposit(
            String msisdn,
            amountrequest request
    ) {

        log.info(
                "Deposit request for {} amount {}",
                msisdn,
                request.getAmount()
        );

        wallet wallet = getWallet(msisdn);

        validateAmount(
                wallet,
                request.getAmount(),
                transactiontype.CREDIT,
                msisdn
        );

        BigDecimal updatedBalance =
                wallet.getBalance()
                        .add(request.getAmount());

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);

        createTransaction(
                wallet,
                transactiontype.CREDIT,
                request.getAmount(),
                updatedBalance,
                transactionstatus.SUCCESS
        );

        return mapWallet(wallet);
    }


    // WITHDRAW
    @Transactional
    public walletresponse withdraw(
            String msisdn,
            amountrequest request
    ) {

        log.info(
                "Withdraw request for {} amount {}",
                msisdn,
                request.getAmount()
        );

        wallet wallet = getWallet(msisdn);

        validateAmount(
                wallet,
                request.getAmount(),
                transactiontype.DEBIT,
                msisdn
        );

        if (wallet.getBalance()
                .compareTo(request.getAmount()) < 0) {

            createTransaction(
                    wallet,
                    transactiontype.DEBIT,
                    request.getAmount(),
                    wallet.getBalance(),
                    transactionstatus.FAILED
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

        createTransaction(
                wallet,
                transactiontype.DEBIT,
                request.getAmount(),
                updatedBalance,
                transactionstatus.SUCCESS
        );

        return mapWallet(wallet);
    }


    // GET ALL TRANSACTIONS
    @Transactional(readOnly = true)
    public List<transactionresponse>
    getTransactions(String msisdn) {

        wallet wallet = getWallet(msisdn);

        return transactionRepository
                .findByWalletOrderByCreatedAtDesc(
                        wallet
                )
                .stream()
                .map(this::mapTransaction)
                .collect(Collectors.toList());
    }


    // GET SINGLE TRANSACTION
    @Transactional(readOnly = true)
    public transactionresponse
    getTransactionByReferenceId(
            String referenceId
    ) {

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

//get transactions by status and type
    @Transactional(readOnly = true)
    public List<transactionresponse>
    getTransactionsByFilters(

            transactiontype type,

            transactionstatus status
    ) {

        List<transaction> transactions;

        if (type != null && status != null) {

            transactions =
                    transactionRepository
                            .findByTransactionTypeAndStatusOrderByCreatedAtDesc(
                                    type,
                                    status
                            );

        } else if (type != null) {

            transactions =
                    transactionRepository
                            .findByTransactionTypeOrderByCreatedAtDesc(
                                    type
                            );

        } else if (status != null) {

            transactions =
                    transactionRepository
                            .findByStatusOrderByCreatedAtDesc(
                                    status
                            );

        } else {

            transactions =
                    transactionRepository.findAll();
        }

        return transactions.stream()
                .map(this::mapTransaction)
                .toList();
    }
//get user transactions
@Transactional(readOnly = true)
public List<transactionresponse>
getUserTransactions(

        String msisdn,

        transactiontype type,

        transactionstatus status
) {

    wallet wallet = getWallet(msisdn);

    List<transaction> transactions;

    if (type != null && status != null) {

        transactions =
                transactionRepository
                        .findByWalletAndTransactionTypeAndStatusOrderByCreatedAtDesc(
                                wallet,
                                type,
                                status
                        );

    } else if (type != null) {

        transactions =
                transactionRepository
                        .findByWalletAndTransactionTypeOrderByCreatedAtDesc(
                                wallet,
                                type
                        );

    } else if (status != null) {

        transactions =
                transactionRepository
                        .findByWalletAndStatusOrderByCreatedAtDesc(
                                wallet,
                                status
                        );

    } else {

        transactions =
                transactionRepository
                        .findByWalletOrderByCreatedAtDesc(
                                wallet
                        );
    }

    return transactions.stream()
            .map(this::mapTransaction)
            .toList();
}

    // GET ALL USERS
    @Transactional(readOnly = true)
    public List<walletresponse>
    getAllUsers(
            int page,
            int size
    ) {

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

        walletRepository.delete(wallet);

        log.info(
                "Deleted user: {}",
                msisdn
        );
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


    // VALIDATE AMOUNT
    private void validateAmount(
            wallet wallet,
            BigDecimal amount,
            transactiontype type,
            String msisdn
    ) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            createTransaction(
                    wallet,
                    type,
                    amount,
                    wallet.getBalance(),
                    transactionstatus.FAILED
            );

            log.warn(
                    "Invalid amount request from {}",
                    msisdn
            );

            throw new customexception(
                    "Amount must be greater than zero",
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    // CREATE TRANSACTION
    private void createTransaction(
            wallet wallet,
            transactiontype type,
            BigDecimal amount,
            BigDecimal balance,
            transactionstatus status
    ) {

        transaction transaction =
                new transaction();

        transaction.setReferenceId(
                generateReferenceId()
        );

        transaction.setTransactionType(type);

        transaction.setAmount(amount);

        transaction.setAvailableBalance(balance);

        transaction.setStatus(status);

        transaction.setWallet(wallet);

        transactionservice
                .saveTransaction(transaction);
    }


    // MAP WALLET RESPONSE
    private walletresponse mapWallet(
            wallet wallet
    ) {

        walletresponse response =
                new walletresponse();

        response.setId(wallet.getId());

        response.setMsisdn(
                wallet.getMsisdn()
        );

        response.setBalance(
                wallet.getBalance()
        );

        response.setCreatedAt(
                wallet.getCreatedAt()
        );

        return response;
    }


    // MAP TRANSACTION RESPONSE
    private transactionresponse
    mapTransaction(
            transaction transaction
    ) {

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