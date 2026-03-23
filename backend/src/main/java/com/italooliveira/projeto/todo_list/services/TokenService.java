package com.italooliveira.projeto.todo_list.services;

import com.italooliveira.projeto.todo_list.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey key;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(7200))) // 2 horas
                .signWith(key)
                .compact();
    }

    public String validateToken(String token) {
        try {
            // Na 0.12.5, o parser é fluído e usamos verifyWith
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            // Se o token for inválido, expirado ou a chave for diferente, retorna null
            return null;
        }
    }
}