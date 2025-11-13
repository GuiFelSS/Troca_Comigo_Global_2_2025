package br.com.fiap.globalSolution.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    // --- MÉTODOS MANUAIS ---
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}