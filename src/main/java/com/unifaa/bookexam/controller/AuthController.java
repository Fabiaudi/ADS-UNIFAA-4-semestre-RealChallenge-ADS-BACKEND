package com.unifaa.bookexam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.unifaa.bookexam.model.dto.*;
import com.unifaa.bookexam.model.entity.Student;
import com.unifaa.bookexam.model.entity.User;
import com.unifaa.bookexam.repository.UserRepository;
import com.unifaa.bookexam.util.JwtUtil;

import java.util.Optional;

/**
 * Endpoint de autenticação (login) — fluxo simplificado:
 *  - Busca usuário por email e por matrícula
 *  - Valida senha via PasswordEncoder.matches(raw, hash)
 *  - Gera JWT com claim 'role'
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Tenta buscar o usuário primeiro pelo email. Se não encontrar,
     * tenta buscar pelo ID (matrícula). A credencial de login (email ou ID)
     * é esperada no campo 'email' do LoginRequest.
     */
    private Optional<User> findUserByEmailOrId(String credential) {
        // Tenta buscar por email (único)
        Optional<User> userOpt = userRepository.findByEmail(credential);
        
        if (userOpt.isPresent()) {
            return userOpt;
        }

        // Se não for email, tenta buscar por ID (matrícula, que também é única)
        // Isso cobre logins com IDs como A00001, P00001, E00001
        return userRepository.findById(credential);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        System.out.printf("[AUTH] Tentativa de login credencial=%s%n", req.getEmail());

        // Altera a busca para aceitar email OU ID (matrícula)
        var userOpt = findUserByEmailOrId(req.getEmail());
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        var user = userOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // O token JWT sempre será gerado usando o EMAIL como Subject, 
        // mas a busca de credencial aceita ID.
        String token = jwtUtil.generateToken(user.getEmail(), user.getType());

        String poloId = null;
        if (user instanceof Student student && student.getStudentPolo() != null) {
            poloId = student.getStudentPolo().getId();
        }

        var resp = new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getType(), poloId);
        return ResponseEntity.ok(resp);
    }
}
