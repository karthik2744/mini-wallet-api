package com.mini_wallet_api.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder

public class AdminUserResponse {

    private Long id;

    private String name;

    private String mobileNumber;

    private String role;

    private BigDecimal balance;

    private boolean active;
}