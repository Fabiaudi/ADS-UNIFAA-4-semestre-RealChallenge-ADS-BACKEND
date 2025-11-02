package com.unifaa.bookexam.service;

import com.unifaa.bookexam.model.entity.User;
import com.unifaa.bookexam.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Serviço que implementa a interface {@link UserDetailsService} do Spring Security.
 * É usado pelo AuthenticationManager para buscar os detalhes de um usuário
 * durante o processo de autenticação.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

	/**
     * Construtor para injeção do repositório de usuários.
     *
     * @param users O {@link UserRepository}.
     */
    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

	/**
     * Localiza um usuário pelo seu nome de usuário (que neste projeto é o email).
     *
     * @param username O email do usuário tentando se autenticar.
     * @return Um objeto {@link UserDetails} (do Spring Security) contendo
     * email, senha (hash) e roles.
     * @throws UsernameNotFoundException Se o usuário não for encontrado pelo email.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Busca o usuário pelo email
        User u = users.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
				
		// Define o 'type' (role)
        String role = u.getType() != null ? u.getType() : "ADMIN";
		
		// Constrói o UserDetails do Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .roles(role) // ROLE_ADMIN, ROLE_STUDENT, ROLE_POLO
                .build();
    }
}