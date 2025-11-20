package com.unifaa.bookexam.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
// import com.unifaa.bookexam.model.dto.AuthResponse;
// import com.unifaa.bookexam.model.dto.LoginRequest;
import com.unifaa.bookexam.model.dto.*;
import com.unifaa.bookexam.model.entity.Student;
import com.unifaa.bookexam.repository.UserRepository;
import com.unifaa.bookexam.util.JwtUtil;

/**
 * Endpoint de autenticação (login) — fluxo simplificado:
 *  - Busca usuário por email
 *  - Valida senha via PasswordEncoder.matches(raw, hash)
 *  - Gera JWT com claim 'role'
 *
 * Quando a Laís finalizar o UserDetailsService/AuthenticationProvider,
 * podemos voltar a usar AuthenticationManager.authenticate(...).
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        System.out.printf("[AUTH] Tentativa de login email=%s%n", req.getEmail());

        var userOpt = userRepository.findByEmail(req.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        var user = userOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getType());

        String poloId = null;
        if (user instanceof Student student && student.getStudentPolo() != null) {
            poloId = student.getStudentPolo().getId();
        }

        var resp = new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getType(), poloId);
        return ResponseEntity.ok(resp);
    }
}
