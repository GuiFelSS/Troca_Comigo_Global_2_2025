package br.com.fiap.globalSolution.Controller.SecurityController;

import br.com.fiap.globalSolution.Dto.AuthResponseDto;
import br.com.fiap.globalSolution.Dto.LoginRequestDto;
import br.com.fiap.globalSolution.Dto.RegisterRequestDto;
import br.com.fiap.globalSolution.Security.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // Define a URL base para /auth
public class AuthController {

    @Autowired
    private AuthService authService; // Injeta o serviço que acabamos de criar

    /**
     * Endpoint para REGISTRAR um novo usuário.
     * O app mobile vai fazer um POST para /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request // @Valid aciona as validações do DTO
    ) {
        // Delega toda a lógica para o AuthService
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint para LOGAR um usuário.
     * O app mobile vai fazer um POST para /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        // Delega toda a lógica para o AuthService
        return ResponseEntity.ok(authService.login(request));
    }
}