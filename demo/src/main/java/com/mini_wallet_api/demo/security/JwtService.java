package com.mini_wallet_api.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service

public class JwtService {

    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey12345";

    private Key getSignInKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }

    public String generateToken(
            String mobileNumber,
            String role
    ) {

        return Jwts.builder()

                .subject(mobileNumber)

                .claim("role", role)

                .issuedAt(new Date())

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )

                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }
    public boolean isTokenValid(String token, String mobileNumber) {

        String extractedMobile = extractMobileNumber(token);

        return extractedMobile.equals(mobileNumber)
                && !isTokenExpired(token);
    }
    public String extractMobileNumber(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }
    public String extractRole(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(
                        (SecretKey) getSignInKey()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }
}