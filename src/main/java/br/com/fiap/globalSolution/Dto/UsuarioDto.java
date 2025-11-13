package br.com.fiap.globalSolution.Dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class UsuarioDto {

    @NotBlank(message = "O nome completo é obrigatório")
    private String fullName;

    private String bio;

    @URL(message = "A URL do avatar deve ser válida")
    private String avatarUrl;

    private String location;
    private String timezone;

    @URL(message = "A URL do LinkedIn deve ser válida")
    private String linkedinUrl;

    // --- Getters e Setters Manuais ---

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }
}