package br.com.fiap.globalSolution.Security;

import br.com.fiap.globalSolution.Dto.AuthResponseDto;
import br.com.fiap.globalSolution.Dto.LoginRequestDto;
import br.com.fiap.globalSolution.Dto.RegisterRequestDto;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Rabbit.Entity.EmailEntity;
import br.com.fiap.globalSolution.Rabbit.Service.EmailService;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @Test
    @DisplayName("Deve registrar usuário com sucesso e enviar email")
    void register_Sucesso() {
        // --- CENÁRIO ---
        RegisterRequestDto request = new RegisterRequestDto();
        request.setFullName("Novo Usuário");
        request.setEmail("novo@email.com");
        request.setPassword("123456");

        // Simula que o email NÃO existe
        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Simula encriptação e token
        when(passwordEncoder.encode(any())).thenReturn("senhaCriptografada");
        when(jwtService.generateToken(any(UsuarioEntity.class))).thenReturn("token-jwt-falso");

        // Simula salvamento (retorna o objeto salvo)
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(i -> i.getArgument(0));

        // --- AÇÃO ---
        AuthResponseDto response = authService.register(request);

        // --- VERIFICAÇÃO ---
        assertNotNull(response);
        assertEquals("token-jwt-falso", response.getToken());
        assertEquals("Novo Usuário", response.getUser().getFullName());
        assertEquals(10.0, response.getUser().getTimeCredits()); // Bônus inicial

        // Verifica se a senha foi criptografada antes de salvar
        assertEquals("senhaCriptografada", response.getUser().getPassword());

        // Verifica se o email de boas-vindas foi enviado
        verify(emailService, times(1)).sendEmail(any(EmailEntity.class));
    }

    @Test
    @DisplayName("Não deve registrar se email já existe")
    void register_EmailDuplicado() {
        // --- CENÁRIO ---
        RegisterRequestDto request = new RegisterRequestDto();
        request.setEmail("duplicado@email.com");

        // Simula que o email JÁ existe
        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new UsuarioEntity()));

        // --- AÇÃO E VERIFICAÇÃO ---
        assertThrows(IllegalStateException.class, () -> {
            authService.register(request);
        });

        // Garante que NADA foi salvo
        verify(usuarioRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any());
    }

    @Test
    @DisplayName("Deve realizar login com sucesso")
    void login_Sucesso() {
        // --- CENÁRIO ---
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("teste@email.com");
        request.setPassword("123456");

        UsuarioEntity userEncontrado = new UsuarioEntity();
        userEncontrado.setEmail("teste@email.com");

        // Simula busca do usuário
        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(userEncontrado));
        when(jwtService.generateToken(userEncontrado)).thenReturn("token-jwt-login");

        // --- AÇÃO ---
        AuthResponseDto response = authService.login(request);

        // --- VERIFICAÇÃO ---
        assertNotNull(response);
        assertEquals("token-jwt-login", response.getToken());

        // Verifica se o AuthenticationManager foi chamado (é ele quem valida a senha)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}