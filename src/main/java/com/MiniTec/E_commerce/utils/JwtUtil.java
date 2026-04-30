package com.MiniTec.E_commerce.utils;


import com.MiniTec.E_commerce.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key = Keys.hmacShaKeyFor(
            "jRXZETRrjEvBFc6JBCgELHcO3X03HBJ0y00oTgNNvUCCXm8VKeNSOjQB4RM"
                    .getBytes(StandardCharsets.UTF_8)
    );

    private Claims getClaims(String token) {
        return Jwts.parser()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private boolean isTokenValid(String token, UserDetails userdetails) {
        String username = extractUsername(token);
        return username.equals(userdetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(User user) {
        long expirationMillis = 1000 * 60 * 60 *24;//24 HORAS
        Date now= new Date();
        Date expiry = new Date(now.getTime()+expirationMillis);
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }




}
