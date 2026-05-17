package com.mini_wallet_api.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class transactionresponse {

    private String referenceId;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal availableBalance;

    private String status;

    private LocalDateTime createdAt;
}