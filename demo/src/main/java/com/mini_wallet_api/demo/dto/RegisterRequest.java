package com.mini_wallet_api.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String password;
}