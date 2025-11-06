package com.unifaa.bookexam.controller;

import com.unifaa.bookexam.model.dto.*;
import com.unifaa.bookexam.repository.UserRepository;
import com.unifaa.bookexam.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para lidar com endpoints de autenticação.
 * Expõe a rota /api/auth/login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

	/**
     * Construtor para injeção de dependências do AuthenticationManager,
     * UserRepository e JwtUtil.
     *
     * @param am O AuthenticationManager do Spring Security.
     * @param ur O repositório de usuários.
     * @param ju O utilitário JWT.
     */
    public AuthController(AuthenticationManager am, UserRepository ur, JwtUtil ju) {
        this.authenticationManager = am;
        this.userRepository = ur;
        this.jwtUtil = ju;
    }

    /**
     * Endpoint para login (autenticação) de usuários.
     * Recebe credenciais (email e senha) e, se válidas, retorna um
     * {@link AuthResponse} contendo o token JWT e dados básicos do usuário.
     *
     * @param req O DTO {@link LoginRequest} contendo email e senha.
     * @return Um ResponseEntity 200 (OK) com o AuthResponse se o login for bem-sucedido,
     * ou 401 (Unauthorized) se as credenciais forem inválidas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        System.out.println("Tentativa de login para Email: [" + req.getEmail() + "], Senha: [" + req.getPassword() + "]");
        
        // Tenta autenticar usando o AuthenticationManager
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

		// Se autenticado, busca o usuário no banco para obter dados
        var user = userRepository.findByEmail(req.getEmail()).orElseThrow();
		
		// Gera o token JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getType());
		
		// Prepara a resposta
        var resp = new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getType());
        return ResponseEntity.ok(resp);
    }
}