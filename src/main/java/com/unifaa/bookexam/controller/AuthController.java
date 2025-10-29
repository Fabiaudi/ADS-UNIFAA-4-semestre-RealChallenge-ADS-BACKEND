package com.unifaa.bookexam.controller;

import com.unifaa.bookexam.model.dto.*;
import com.unifaa.bookexam.repository.UserRepository;
import com.unifaa.bookexam.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager am, UserRepository ur, JwtUtil ju) {
        this.authenticationManager = am;
        this.userRepository = ur;
        this.jwtUtil = ju;
    }

    // Login endpoint que retorna JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        // Autentica credenciais
        // Gera token JWT
        // Retorna AuthResponse
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        var user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user.getEmail(), user.getType());
        var resp = new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getType());
        return ResponseEntity.ok(resp);
    }
}