package com.mini_wallet_api.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String password;
}