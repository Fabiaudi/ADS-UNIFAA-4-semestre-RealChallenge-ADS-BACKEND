package com.unifaa.bookexam.model.dto;

import lombok.*;

/**
 * DTO (Data Transfer Object) para a resposta de autenticação.
 * Retorna o token JWT e informações básicas do usuário logado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	
	/**
     * O token JWT gerado.
     */
    private String token;
	
	/**
     * O ID (matrícula) do usuário.
     */
    private String id;
	
	/**
     * O nome do usuário.
     */
    private String name;
	
	/**
     * O email do usuário.
     */
    private String email;
	
	/**
     * O tipo (role) do usuário (ADMIN, STUDENT, POLO).
     */
    private String type;
}