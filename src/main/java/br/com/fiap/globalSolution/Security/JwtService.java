package br.com.fiap.globalSolution.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // 1. Injeta os valores do application.properties
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    // 2. Extrai o username (email) do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. Extrai um "claim" (dado) específico do token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 4. Gera um token para um UserDetails (UsuarioEntity)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // 5. Gera o token com claims extras (se necessário)
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername()) // Define o "subject" como o email
                .issuedAt(new Date(System.currentTimeMillis())) // Data de criação
                .expiration(new Date(System.currentTimeMillis() + expirationMs)) // Data de expiração
                .signWith(getSignInKey(), Jwts.SIG.HS256) // Assina com a chave
                .compact();
    }

    // 6. Valida se o token é válido para aquele usuário
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Verifica se o username do token é o mesmo do UserDetails E se não está expirado
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 7. Verifica se o token está expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 8. Extrai a data de expiração do token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 9. Extrai TODOS os dados (claims) do token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 10. Converte a chave secreta (String) para um objeto SecretKey
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}