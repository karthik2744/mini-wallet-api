package com.mini_wallet_api.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class WalletResponse {

    private Long id;

    private String msisdn;

    private BigDecimal balance;
}