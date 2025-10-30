package com.unifaa.bookexam.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Componente utilitário para lidar com a criação e validação de Tokens JWT.
 * Utiliza valores do application.properties (jwt.secret, jwt.expiration-ms).
 */
@Component
public class JwtUtil {

	/**
     * Chave secreta para assinar o token, injetada do application.properties.
     */
    @Value("${jwt.secret:senha-secreta}")
    private String secret;

	/**
     * Tempo de expiração do token em milissegundos, injetado do application.properties.
     */
    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    /**
     * Gera um novo token JWT para um usuário.
     *
     * @param subject O "subject" do token (geralmente o email ou ID do usuário).
     * @param role    A "role" (tipo) do usuário (ex: ADMIN, STUDENT),
     * armazenada como uma 'claim' customizada.
     * @return Uma string representando o token JWT assinado.
     */
    public String generateToken(String subject, String role) {
        Algorithm alg = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role) // Adiciona a role como uma claim
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(alg);
    }

    /**
     * Valida e decodifica um token JWT.
     * Verifica a assinatura e a validade (expiração) do token.
     *
     * @param token O token JWT (string) a ser validado.
     * @return Um objeto {@link DecodedJWT} se o token for válido.
     * @throws com.auth0.jwt.exceptions.JWTVerificationException Se o token for inválido
     * (expirado, assinatura incorreta, etc.).
     */
    public DecodedJWT validateToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secret);
        return JWT.require(alg).build().verify(token);
    }
}