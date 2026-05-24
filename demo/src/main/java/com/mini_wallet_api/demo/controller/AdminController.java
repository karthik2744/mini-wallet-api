package com.mini_wallet_api.demo.controller;

import com.mini_wallet_api.demo.dto.AdminDashboardResponse;
import com.mini_wallet_api.demo.dto.AdminUserResponse;
import com.mini_wallet_api.demo.dto.TransactionAnalyticsResponse;

import com.mini_wallet_api.demo.dto.TransactionResponse;
import com.mini_wallet_api.demo.enums.TransactionStatus;
import com.mini_wallet_api.demo.enums.TransactionType;
import com.mini_wallet_api.demo.service.AdminService;
import com.mini_wallet_api.demo.service.WalletService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor

public class AdminController {

    private final AdminService adminService;

    // ADD THIS
    private final WalletService walletService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse>
    getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboardData()
        );
    }

    @GetMapping("/analytics")
    public ResponseEntity<
            List<TransactionAnalyticsResponse>
            > getAnalytics() {

        return ResponseEntity.ok(

                walletService.getAnalytics()
        );
    }
    @GetMapping("/transactions")

    public ResponseEntity<List<TransactionResponse>>
    getAllTransactions() {

        return ResponseEntity.ok(

                walletService
                        .getAllTransactions()
        );
    }
    @GetMapping("/users")

    public ResponseEntity<
            List<AdminUserResponse>
            > getUsers() {

        return ResponseEntity.ok(

                walletService.getAllUsers()
        );
    }

    @PutMapping("/users/{id}/toggle-status")

    public ResponseEntity<String>
    toggleUserStatus(

            @PathVariable Long id
    ) {

        adminService.toggleUserStatus(id);

        return ResponseEntity.ok(
                "User status updated"
        );
    }

    @GetMapping("/transactions/filter")

    public ResponseEntity<
            Page<TransactionResponse>
            > filterTransactions(

            @RequestParam(
                    defaultValue = ""
            )
            String msisdn,

            @RequestParam(
                    required = false
            )
            TransactionType type,

            @RequestParam(
                    required = false
            )
            TransactionStatus status,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        return ResponseEntity.ok(

                walletService
                        .getFilteredTransactions(

                                msisdn,

                                type,

                                status,

                                page,

                                size
                        )
        );
    }
}