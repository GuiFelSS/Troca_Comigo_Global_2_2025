package br.com.fiap.globalSolution.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Diz ao Spring para gerenciar este componente
public class JwtAuthFilter extends OncePerRequestFilter { // Garante que o filtro rode só uma vez por requisição

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService; // O Spring vai injetar seu CustomUserDetailsService

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Pega o cabeçalho "Authorization" (onde o token vem)
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Verifica se o header existe e se começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Se não tiver token, só passa para o próximo filtro
            return;
        }

        // 3. Extrai o token (remove o "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Extrai o email (username) de dentro do token
        userEmail = jwtService.extractUsername(jwt);

        // 5. Se temos o email E o usuário ainda não está autenticado no contexto do Spring...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Carrega o usuário do banco de dados (usando seu CustomUserDetailsService)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 7. Valida o token (compara com o usuário do banco)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. Se o token for válido, cria a autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credenciais (senha) são nulas para JWT
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9. Coloca o usuário autenticado no Contexto de Segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 10. Passa a requisição para o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }
}