
// Wallet.java (UPDATED)

package com.mini_wallet_api.demo.entity;

import jakarta.persistence.*;
        import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "wallets")
public class wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String msisdn;

    private Double balance;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private user user;

    @OneToMany(mappedBy = "wallet")
    private List<transaction> transactions;

    public Long getId() {
        return id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public List<transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<transaction> transactions) {
        this.transactions = transactions;
    }
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}