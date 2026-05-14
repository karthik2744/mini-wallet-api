// User.java

package com.mini_wallet_api.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String msisdn;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user")
    private wallet Wallet;

    public Long getId() {
        return id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public wallet getWallet() {
        return Wallet;
    }

    public void setWallet(wallet wallet) {
        this.Wallet = wallet;
    }
}