package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.dto.*;
import com.mini_wallet_api.demo.entity.Transaction;
import com.mini_wallet_api.demo.entity.Wallet;
import com.mini_wallet_api.demo.enums.TransactionStatus;
import com.mini_wallet_api.demo.enums.TransactionType;
import com.mini_wallet_api.demo.exception.CustomException;
import com.mini_wallet_api.demo.repository.TransactionRepository;
import com.mini_wallet_api.demo.repository.WalletRepository;
import com.mini_wallet_api.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mini_wallet_api.demo.entity.User;


@Service
@Slf4j
public class WalletService {

    private final UserRepository userRepository;

    private final WalletRepository walletRepository;

    private final TransactionRepository transactionRepository;

    private final TransactionService transactionservice;


    // CONSTRUCTOR INJECTION
    public WalletService(

            WalletRepository walletRepository,

            TransactionRepository transactionRepository,

            TransactionService transactionservice,

            UserRepository userRepository
    ) {

        this.walletRepository =
                walletRepository;

        this.transactionRepository =
                transactionRepository;

        this.transactionservice =
                transactionservice;

        this.userRepository =
                userRepository;
    }


    // CREATE USER
    @Transactional
    public Wallet createUser(String msisdn) {

        log.info(
                "Creating user: {}",
                msisdn
        );

        Wallet existingWallet =
                walletRepository
                        .findByMsisdn(msisdn)
                        .orElse(null);

        if (existingWallet != null) {

            throw new CustomException(
                    "User already exists",
                    HttpStatus.CONFLICT
            );
        }

        Wallet wallet = new Wallet();

        wallet.setMsisdn(msisdn);

        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }


    // GET BALANCE
    @Transactional(readOnly = true)
    public WalletResponse getBalance(
            String msisdn
    ) {

        Wallet wallet = getWallet(msisdn);

        return mapWallet(wallet);
    }


    // DEPOSIT
    @Transactional(
            noRollbackFor = CustomException.class
    )
    public WalletResponse deposit(
            String msisdn,
            AmountRequest request
    ) {

        log.info(
                "Deposit request for {} amount {}",
                msisdn,
                request.getAmount()
        );


        Wallet wallet = getWallet(msisdn);
        validateUserActive(msisdn);

        validateAmount(
                wallet,
                request.getAmount(),
                TransactionType.CREDIT,
                msisdn
        );

        BigDecimal updatedBalance =
                wallet.getBalance()
                        .add(request.getAmount());

        wallet.setBalance(updatedBalance);

        walletRepository.save(wallet);

        createTransaction(
                wallet,
                TransactionType.CREDIT,
                request.getAmount(),
                updatedBalance,
                TransactionStatus.SUCCESS
        );

        return mapWallet(wallet);
    }


    // WITHDRAW
    @Transactional(
            noRollbackFor = CustomException.class
    )
    public WalletResponse withdraw(
            String msisdn,
            AmountRequest request
    ) {

        log.info(
                "Withdraw request for {} amount {}",
                msisdn,
                request.getAmount()
        );

        Wallet wallet = getWallet(msisdn);
        validateUserActive(msisdn);

        validateAmount(
                wallet,
                request.getAmount(),
                TransactionType.DEBIT,
                msisdn
        );

        if (wallet.getBalance()
                .compareTo(request.getAmount()) < 0) {

            createTransaction(
                    wallet,
                    TransactionType.DEBIT,
                    request.getAmount(),
                    wallet.getBalance(),
                    TransactionStatus.FAILED
            );

            transactionRepository.flush();
            log.warn(
                    "Failed withdraw for {} due to insufficient balance",
                    msisdn
            );


            throw new CustomException(
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
                TransactionType.DEBIT,
                request.getAmount(),
                updatedBalance,
                TransactionStatus.SUCCESS
        );

        return mapWallet(wallet);
    }


    // GET ALL TRANSACTIONS
    @Transactional(readOnly = true)
    public List<TransactionResponse>
    getTransactions(String msisdn) {

        Wallet wallet = getWallet(msisdn);

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
    public TransactionResponse
    getTransactionByReferenceId(
            String referenceId
    ) {

        Transaction transaction =
                transactionRepository
                        .findByReferenceId(referenceId)
                        .orElseThrow(() ->
                                new CustomException(
                                        "Transaction not found",
                                        HttpStatus.NOT_FOUND
                                ));

        return mapTransaction(transaction);
    }

//get transactions by status and type
    @Transactional(readOnly = true)
    public List<TransactionResponse>
    getTransactionsByFilters(

            TransactionType type,

            TransactionStatus status
    ) {

        List<Transaction> transactions;

        if (type != null && status != null) {

            transactions =
                    transactionRepository
                            .findByTypeAndStatusOrderByCreatedAtDesc(
                                    type,
                                    status
                            );

        } else if (type != null) {

            transactions =
                    transactionRepository
                            .findByTypeOrderByCreatedAtDesc(
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
public List<TransactionResponse>
getUserTransactions(

        String msisdn,

        TransactionType type,

        TransactionStatus status
) {

    Wallet wallet = getWallet(msisdn);

    List<Transaction> transactions;

    if (type != null && status != null) {

        transactions =
                transactionRepository
                        .findByWalletAndTypeAndStatusOrderByCreatedAtDesc(
                                wallet,
                                type,
                                status
                        );

    } else if (type != null) {

        transactions =
                transactionRepository
                        .findByWalletAndTypeOrderByCreatedAtDesc(
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
    public List<WalletResponse>
    getAllUsers(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()
                );

        return walletRepository
                .findAll(pageable)
                .stream()
                .map(this::mapWallet)
                .collect(Collectors.toList());
    }


    // DELETE USER
    @Transactional
    public void deleteUser(String msisdn) {

        Wallet wallet = getWallet(msisdn);

        walletRepository.delete(wallet);

        log.info(
                "Deleted user: {}",
                msisdn
        );
    }


    // GET WALLET HELPER
    private Wallet getWallet(String msisdn) {

        return walletRepository
                .findByMsisdn(msisdn)
                .orElseThrow(() ->
                        new CustomException(
                                "User not found",
                                HttpStatus.NOT_FOUND
                        ));
    }


    // VALIDATE AMOUNT
    private void validateAmount(

            Wallet wallet,

            BigDecimal amount,

            TransactionType type,

            String msisdn
    ) {

        if (amount == null ||

                amount.compareTo(BigDecimal.ZERO) <= 0) {

            createTransaction(

                    wallet,

                    type,

                    amount,

                    wallet.getBalance(),

                    TransactionStatus.FAILED
            );

            transactionRepository.flush();

            log.warn(
                    "Invalid amount entered by {} amount {}",
                    msisdn,
                    amount
            );

            throw new CustomException(

                    "Amount must be greater than 0",

                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // CREATE TRANSACTION
    private void createTransaction(
            Wallet wallet,
            TransactionType type,
            BigDecimal amount,
            BigDecimal balance,
            TransactionStatus status
    ) {

        Transaction transaction =
                new Transaction();

        transaction.setReferenceId(
                generateReferenceId()
        );

        transaction.setType(type);

        transaction.setAmount(amount);

        transaction.setAvailableBalance(balance);

        transaction.setStatus(status);

        transaction.setWallet(wallet);

        transactionservice
                .saveTransaction(transaction);
    }


    // MAP WALLET RESPONSE
    private WalletResponse
    mapWallet(
            Wallet wallet
    ) {

        WalletResponse response =
                new WalletResponse();

        response.setId(
                wallet.getId()
        );

        response.setMsisdn(
                wallet.getMsisdn()
        );

        response.setBalance(
                wallet.getBalance()
        );

        return response;
    }

    // MAP TRANSACTION RESPONSE
    private TransactionResponse
    mapTransaction(
            Transaction transaction
    ) {

        TransactionResponse response =
                new TransactionResponse();

        response.setReferenceId(
                transaction.getReferenceId()
        );

        response.setMobileNumber(
                transaction.getWallet()
                        .getMsisdn()
        );

        response.setType(
                transaction.getType()
        );

        response.setAmount(
                transaction.getAmount()
        );

        response.setAvailableBalance(
                transaction.getAvailableBalance()
        );

        response.setStatus(
                transaction.getStatus()
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

    public List<TransactionAnalyticsResponse>
    getAnalytics() {

        return transactionRepository
                .getTransactionAnalytics();
    }

    public List<TransactionResponse>
    getAllTransactions() {

        return transactionRepository

                .findAll()

                .stream()

                .sorted((a, b) ->

                        b.getCreatedAt()
                                .compareTo(
                                        a.getCreatedAt()
                                )
                )

                .map(this::mapTransaction)

                .toList();
    }

    public List<AdminUserResponse>
    getAllUsers() {

        return userRepository

                .findAll()

                .stream()

                .map(user -> {

                    Wallet wallet =
                            walletRepository
                                    .findByMsisdn(
                                            user.getMobileNumber()
                                    )
                                    .orElse(null);

                    return AdminUserResponse
                            .builder()

                            .id(
                                    user.getId()
                            )

                            .name(
                                    user.getName()
                            )

                            .mobileNumber(
                                    user.getMobileNumber()
                            )

                            .role(
                                    user.getRole().name()
                            )

                            .balance(

                                    wallet != null

                                            ? wallet.getBalance()

                                            : BigDecimal.ZERO
                            )
                            .active(
                                    user.isActive()
                            )

                            .build();
                })

                .toList();
    }

    @Transactional(readOnly = true)

    public Page<TransactionResponse>
    getFilteredTransactions(

            String msisdn,

            TransactionType type,

            TransactionStatus status,

            int page,

            int size
    ) {

        Pageable pageable =

                PageRequest.of(

                        page,

                        size,

                        Sort.by("createdAt")
                                .descending()
                );

        Page<Transaction> transactions;

        if (type != null && status != null) {

            transactions =

                    transactionRepository

                            .findByWallet_MsisdnContainingAndTypeAndStatus(

                                    msisdn,

                                    type,

                                    status,

                                    pageable
                            );

        } else if (type != null) {

            transactions =

                    transactionRepository

                            .findByWallet_MsisdnContainingAndType(

                                    msisdn,

                                    type,

                                    pageable
                            );

        } else if (status != null) {

            transactions =

                    transactionRepository

                            .findByWallet_MsisdnContainingAndStatus(

                                    msisdn,

                                    status,

                                    pageable
                            );

        } else {

            transactions =

                    transactionRepository

                            .findByWallet_MsisdnContaining(

                                    msisdn,

                                    pageable
                            );
        }

        return transactions.map(
                this::mapTransaction
        );
    }

    private void validateUserActive(
            String msisdn
    ) {

        User user =

                userRepository
                        .findByMobileNumber(
                                msisdn
                        )

                        .orElseThrow(() ->

                                new CustomException(

                                        "User not found",

                                        HttpStatus.NOT_FOUND
                                )
                        );

        if (!user.isActive()) {

            throw new CustomException(

                    "Inactive users cannot perform transactions",

                    HttpStatus.FORBIDDEN
            );
        }
    }

}