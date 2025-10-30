package com.unifaa.bookexam.config;

import com.unifaa.bookexam.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Filtro de autenticação JWT que intercepta todas as requisições.
 * Este filtro é executado uma vez por requisição (OncePerRequestFilter)
 * para validar o token JWT e configurar o contexto de segurança do Spring.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

	/**
     * Construtor que injeta o utilitário JWT.
     *
     * @param jwtUtil O utilitário para validar tokens.
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

	/**
     * Lógica principal do filtro.
     * Extrai o token do header "Authorization", valida-o e, se for válido,
     * define o usuário autenticado no {@link SecurityContextHolder}.
     *
     * @param request     A requisição HTTP.
     * @param response    A resposta HTTP.
     * @param filterChain A cadeia de filtros.
     * @throws ServletException Se ocorrer um erro de servlet.
     * @throws IOException      Se ocorrer um erro de I/O.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                DecodedJWT decoded = jwtUtil.validateToken(token);
                String email = decoded.getSubject();
                String role = decoded.getClaim("role").asString();
				
				// Cria o objeto de autenticação para o Spring Security
                var auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
				
				// Define o usuário como autenticado no contexto
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
				// Se o token for inválido (expirado, assinatura errada), limpa o contexto
                SecurityContextHolder.clearContext();
            }
        }

		// Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}