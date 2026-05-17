package com.mini_wallet_api.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class amountrequest {

    @NotNull(
            message = "Amount is required"
    )
    @DecimalMin(
            value = "0.01",
            message =
                    "Amount must be greater than zero"
    )
    private BigDecimal amount;
}