package com.unifaa.bookexam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:senha-secreta}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    // Gera token JWT com subject (email) e role do usuário
    public String generateToken(String subject, String role) {
        Algorithm alg = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(alg);
    }

    // Valida e decodifica token JWT
    public DecodedJWT validateToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        return JWT.require(alg).build().verify(token);
    }
}