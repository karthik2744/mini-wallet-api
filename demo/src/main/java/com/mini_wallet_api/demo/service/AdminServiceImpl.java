package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.dto.AdminDashboardResponse;

import com.mini_wallet_api.demo.entity.User;

import com.mini_wallet_api.demo.exception.CustomException;

import com.mini_wallet_api.demo.repository.TransactionRepository;

import com.mini_wallet_api.demo.repository.UserRepository;

import com.mini_wallet_api.demo.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class AdminServiceImpl
        implements AdminService {

    private final UserRepository
            userRepository;

    private final WalletRepository
            walletRepository;

    private final TransactionRepository
            transactionRepository;

    @Override
    public AdminDashboardResponse
    getDashboardData() {

        return AdminDashboardResponse
                .builder()

                .totalUsers(

                        userRepository.count()
                )

                .totalWalletBalance(

                        walletRepository
                                .getTotalWalletBalance()
                )

                .totalTransactions(

                        transactionRepository
                                .count()
                )

                .failedTransactions(

                        transactionRepository
                                .countByStatus(

                                        com.mini_wallet_api.demo.enums.TransactionStatus.FAILED
                                )
                )

                .creditTransactions(

                        transactionRepository
                                .countByTypeAndStatus(

                                        com.mini_wallet_api.demo.enums.TransactionType.CREDIT,

                                        com.mini_wallet_api.demo.enums.TransactionStatus.SUCCESS
                                )
                )

                .debitTransactions(

                        transactionRepository
                                .countByTypeAndStatus(

                                        com.mini_wallet_api.demo.enums.TransactionType.DEBIT,

                                        com.mini_wallet_api.demo.enums.TransactionStatus.SUCCESS
                                )
                )

                .build();
    }

    @Override
    @Transactional

    public void toggleUserStatus(
            Long id
    ) {

        User user =

                userRepository
                        .findById(id)

                        .orElseThrow(() ->

                                new CustomException(

                                        "User not found",

                                        HttpStatus.NOT_FOUND
                                )
                        );

        user.setActive(
                !user.isActive()
        );

        userRepository.save(user);
    }
}