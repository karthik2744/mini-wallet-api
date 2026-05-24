package com.mini_wallet_api.demo.dto;

import com.mini_wallet_api.demo.enums.TransactionStatus;
import com.mini_wallet_api.demo.enums.TransactionType;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

public class TransactionResponse {

    private String referenceId;

    private String mobileNumber;

    private TransactionType type;

    private BigDecimal amount;

    private BigDecimal availableBalance;

    private TransactionStatus status;

    private LocalDateTime createdAt;
}