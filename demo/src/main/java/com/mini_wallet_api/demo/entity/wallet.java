package com.mini_wallet_api.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "wallets",
        indexes = {
                @Index(name = "idx_msisdn",
                        columnList = "msisdn")
        }
)
@Data
public class wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String msisdn;

    @Column(nullable = false)
    private BigDecimal balance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "wallet")
    private List<transaction> transactions;
}