package com.mini_wallet_api.demo.controller;

import com.mini_wallet_api.demo.dto.*;
import com.mini_wallet_api.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        return authService.login(request);
    }
    @GetMapping("/test")
    public String test() {

        return "Protected API working";
    }

    @GetMapping("/encode")
    public String encode() {

        return passwordEncoder.encode("admin123");
    }
    @GetMapping("/me")
    public UserProfileResponse me(
            Authentication authentication
    ) {

        String mobileNumber =
                authentication.getName();

        return authService
                .getCurrentUser(mobileNumber);
    }
}