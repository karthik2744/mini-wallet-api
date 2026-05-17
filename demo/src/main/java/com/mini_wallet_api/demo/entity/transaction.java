package com.mini_wallet_api.demo.entity;

import com.mini_wallet_api.demo.enums.transactionstatus;
import com.mini_wallet_api.demo.enums.transactiontype;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String referenceId;

    @Enumerated(EnumType.STRING)
    private transactiontype transactionType;
    private BigDecimal amount;

    private BigDecimal availableBalance;

    @Enumerated(EnumType.STRING)
    private transactionstatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private wallet wallet;
}