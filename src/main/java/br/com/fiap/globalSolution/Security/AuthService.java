package br.com.fiap.globalSolution.Security;

import br.com.fiap.globalSolution.Dto.AuthResponseDto;
import br.com.fiap.globalSolution.Dto.LoginRequestDto;
import br.com.fiap.globalSolution.Dto.RegisterRequestDto;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Enum.Role;
import br.com.fiap.globalSolution.Rabbit.Entity.EmailEntity;
import br.com.fiap.globalSolution.Rabbit.Service.EmailService;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email já está em uso");
        }

        UsuarioEntity user = UsuarioEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .timeCredits(10.0)
                .build();

        usuarioRepository.save(user);
        String token = jwtService.generateToken(user);

        // ** CORREÇÃO AQUI **
        // Vamos usar user.getUsername() (que retorna o email)
        // e user.getFullName() (que agora existe)
        sendWelcomeEmail(user.getUsername(), user.getFullName());

        return AuthResponseDto.builder()
                .token(token)
                .user(user)
                .build();
    }

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UsuarioEntity user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String token = jwtService.generateToken(user);

        return AuthResponseDto.builder()
                .token(token)
                .user(user)
                .build();
    }

    private void sendWelcomeEmail(String emailTo, String name) {
        EmailEntity email = new EmailEntity();
        email.setEmailFrom(emailFrom);
        email.setEmailTo(emailTo);
        email.setOwnerRef("TrocaComigoApp");
        email.setSubject("Bem-vindo ao Troca Comigo, " + name + "!");
        email.setText("Olá " + name + ",\n\nSeu registro foi concluído com sucesso. Estamos felizes em ter você conosco!\n\nUse seus 10 créditos iniciais para aprender algo novo.\n\nAtenciosamente,\nEquipe Troca Comigo");

        emailService.sendEmail(email);
    }
}