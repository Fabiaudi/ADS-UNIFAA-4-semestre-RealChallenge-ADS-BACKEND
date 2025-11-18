package com.unifaa.bookexam.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Utilitário de geração/validação de JWT.
 * Claim usada para perfil: 'role'.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:senha-secreta}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}") // 24h default
    private long expirationMs;

    public String generateToken(String subjectEmail, String role) {
        Algorithm alg = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(subjectEmail)
                .withClaim("role", role) // ex: ADMIN | POLO | STUDENT
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + expirationMs))
                .sign(alg);
    }

    public DecodedJWT validateToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
        return JWT.require(alg).build().verify(token);
    }
}
