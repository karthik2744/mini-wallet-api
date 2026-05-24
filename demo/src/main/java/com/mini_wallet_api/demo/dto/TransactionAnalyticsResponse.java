package com.mini_wallet_api.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data

public class TransactionAnalyticsResponse {

    private Object date;

    private Long transactionCount;

    private BigDecimal totalCredit;

    private BigDecimal totalDebit;

    // IMPORTANT CONSTRUCTOR
    public TransactionAnalyticsResponse(

            Object date,

            Long transactionCount,

            BigDecimal totalCredit,

            BigDecimal totalDebit
    ) {

        this.date = date;

        this.transactionCount =
                transactionCount;

        this.totalCredit =
                totalCredit;

        this.totalDebit =
                totalDebit;
    }
}