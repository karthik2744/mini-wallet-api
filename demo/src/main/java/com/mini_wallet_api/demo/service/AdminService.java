package com.mini_wallet_api.demo.service;

import com.mini_wallet_api.demo.dto.AdminDashboardResponse;

public interface AdminService {

    AdminDashboardResponse
    getDashboardData();

    void toggleUserStatus(
            Long id
    );
}