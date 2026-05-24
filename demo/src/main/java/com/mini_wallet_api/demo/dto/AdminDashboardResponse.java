package com.mini_wallet_api.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder

public class AdminDashboardResponse {

    private Long totalUsers;

    private BigDecimal totalWalletBalance;

    private Long totalTransactions;

    private Long failedTransactions;

    private Long creditTransactions;

    private Long debitTransactions;
}