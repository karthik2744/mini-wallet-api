package com.mini_wallet_api.demo.entity;

import com.mini_wallet_api.demo.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String mobileNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;   // ✅ your enum, not Spring's @Role

    @Column(nullable = false)

    private boolean active = true;
}