package com.mini_wallet_api.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class walletresponse {

    private Long id;

    private String msisdn;

    private BigDecimal balance;

    private LocalDateTime createdAt;
}