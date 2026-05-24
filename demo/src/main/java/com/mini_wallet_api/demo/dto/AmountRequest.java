package com.mini_wallet_api.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountRequest {

    @NotNull

    @DecimalMin("1")

    private BigDecimal amount;

}