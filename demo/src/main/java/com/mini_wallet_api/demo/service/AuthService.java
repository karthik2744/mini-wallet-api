package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.dto.LoginRequest;
import com.mini_wallet_api.demo.dto.RegisterRequest;
import com.mini_wallet_api.demo.dto.UserProfileResponse;

import com.mini_wallet_api.demo.entity.User;

import com.mini_wallet_api.demo.enums.Role;

import com.mini_wallet_api.demo.exception.CustomException;
import com.mini_wallet_api.demo.repository.UserRepository;

import com.mini_wallet_api.demo.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // =========================================
    // REGISTER
    // =========================================

    public String register(
            RegisterRequest request
    ) {

        log.info(
                "Register request received for {}",
                request.getMobileNumber()
        );

        if (userRepository.findByMobileNumber(
                request.getMobileNumber()
        ).isPresent()) {

            log.warn(
                    "Registration failed. Mobile already exists {}",
                    request.getMobileNumber()
            );

            throw new CustomException(

                    "User already exists",

                    HttpStatus.CONFLICT
            );
        }

        User newUser = User.builder()

                .name(
                        request.getName()
                )

                .mobileNumber(
                        request.getMobileNumber()
                )

                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )

                .role(Role.USER)

                .build();

        userRepository.save(newUser);

        log.info(
                "User registered successfully {}",
                request.getMobileNumber()
        );

        return "User registered successfully";
    }

    // =========================================
    // LOGIN
    // =========================================

    public String login(
            LoginRequest request
    ) {

        log.info(
                "Login request received for {}",
                request.getMobileNumber()
        );

        User existingUser =
                userRepository
                        .findByMobileNumber(
                                request.getMobileNumber()
                        )
                        .orElseThrow(() -> {

                            log.warn(
                                    "Login failed. User not found {}",
                                    request.getMobileNumber()
                            );

                            return new RuntimeException(
                                    "User not found"
                            );
                        });

        boolean passwordMatches =
                passwordEncoder.matches(

                        request.getPassword(),

                        existingUser.getPassword()
                );

        if (!passwordMatches) {

            log.warn(
                    "Invalid password attempt for {}",
                    request.getMobileNumber()
            );

            throw new RuntimeException(
                    "Invalid password"
            );
        }

        log.info(
                "User logged in successfully {}",
                request.getMobileNumber()
        );

        return jwtService.generateToken(

                existingUser.getMobileNumber(),

                existingUser.getRole().name()
        );
    }

    // =========================================
    // CURRENT USER
    // =========================================

    public UserProfileResponse
    getCurrentUser(
            String mobileNumber
    ) {

        log.info(
                "Fetching current user {}",
                mobileNumber
        );

        User user =
                userRepository
                        .findByMobileNumber(
                                mobileNumber
                        )
                        .orElseThrow(() ->

                                new RuntimeException(
                                        "User not found"
                                )
                        );

        return UserProfileResponse.builder()

                .id(
                        user.getId()
                )

                .name(
                        user.getName()
                )

                .mobileNumber(
                        user.getMobileNumber()
                )

                .role(
                        user.getRole().name()
                )

                .active(
                        user.isActive()
                )

                .build();
    }
}