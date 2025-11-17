package com.unifaa.bookexam.model.dto;

import lombok.*;

/**
 * DTO (Data Transfer Object) para a requisição de login.
 * Carrega as credenciais (email e senha) enviadas pelo cliente.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	
	/**
     * O email do usuário.
     */
    private String email;
	
	/**
     * A senha em texto plano do usuário.
     */
    private String password;
}