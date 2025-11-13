package br.com.fiap.globalSolution.Dto;

import br.com.fiap.globalSolution.Entity.UsuarioEntity;

public class AuthResponseDto {

    private String token;
    private UsuarioEntity user;

    // --- MÉTODOS MANUAIS ---

    public AuthResponseDto() {
    }

    public AuthResponseDto(String token, UsuarioEntity user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioEntity getUser() {
        return user;
    }

    public void setUser(UsuarioEntity user) {
        this.user = user;
    }

    // Método estático que inicia o Builder
    public static AuthResponseDtoBuilder builder() {
        return new AuthResponseDtoBuilder();
    }

    // Classe interna do Builder
    public static class AuthResponseDtoBuilder {
        private String token;
        private UsuarioEntity user;

        public AuthResponseDtoBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AuthResponseDtoBuilder user(UsuarioEntity user) {
            this.user = user;
            return this;
        }

        public AuthResponseDto build() {
            return new AuthResponseDto(this.token, this.user);
        }
    }
}