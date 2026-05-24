package com.mini_wallet_api.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UserProfileResponse {

    private Long id;

    private String name;

    private String mobileNumber;

    private String role;

    private boolean active;
}