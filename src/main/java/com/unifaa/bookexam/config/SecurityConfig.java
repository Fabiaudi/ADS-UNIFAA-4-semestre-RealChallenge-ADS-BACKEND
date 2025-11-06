package com.unifaa.bookexam.config;

import com.unifaa.bookexam.service.CustomUserDetailsService;
import com.unifaa.bookexam.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 * Configuração central de segurança do Spring Security.
 * Define o {@link SecurityFilterChain}, {@link PasswordEncoder}, {@link AuthenticationManager}
 * e a configuração de CORS.
 */
@SuppressWarnings("unused")
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Construtor para injeção de dependências do serviço de usuário e do utilitário JWT.
     *
     * @param uds Serviço customizado de detalhes do usuário.
     * @param jwtUtil Utilitário para manipulação de tokens JWT.
     */
    public SecurityConfig(CustomUserDetailsService uds, JwtUtil jwtUtil) {
        this.userDetailsService = uds;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Configura a cadeia de filtros de segurança (SecurityFilterChain).
     * Define quais rotas são públicas ou privadas, desabilita CSRF,
     * define a política de sessão como STATELESS e adiciona o filtro JWT.
     *
     * @param http O construtor HttpSecurity.
     * @return O SecurityFilterChain construído.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Permite /api/auth/** sem autenticação
        // Requer autenticação para demais endpoints
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

	/**
     * Define a configuração de CORS (Cross-Origin Resource Sharing) da aplicação.
     * Permite requisições de qualquer origem, método e header (configuração liberal para desenvolvimento).
     *
     * @return A fonte de configuração de CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Expõe o AuthenticationManager como um Bean.
     * Configura o provedor de autenticação para usar o {@link CustomUserDetailsService}
     * e o {@link PasswordEncoder}.
     *
     * @param http O construtor HttpSecurity.
     * @return O AuthenticationManager.
     * @throws Exception Se ocorrer um erro na construção.
     */
    /*
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder amb = http.getSharedObject(AuthenticationManagerBuilder.class);
        amb.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return amb.build();
    }
    */

    /**
     * Define o provedor de autenticação (DAO).
     * É aqui que vinculamos o nosso CustomUserDetailsService e o PasswordEncoder.
     * O Spring (ProviderManager) usará este provider automaticamente.
     *
     * @return O DaoAuthenticationProvider configurado.
     */
    @SuppressWarnings("deprecation")
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Nosso serviço de usuário
        authProvider.setPasswordEncoder(passwordEncoder()); // Nosso encoder BCrypt
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager global como um Bean.
     * Este é o bean que será injetado no AuthController.
     *
     * @param config A configuração de autenticação do Spring.
     * @return O AuthenticationManager.
     * @throws Exception Se ocorrer um erro.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

	/**
     * Define o encoder de senhas (BCrypt) como um Bean.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}