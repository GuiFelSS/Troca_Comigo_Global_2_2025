package br.com.fiap.globalSolution.Dto;

import jakarta.validation.constraints.NotBlank;

public class IaRequestDto {

    @NotBlank(message = "As palavras-chave são obrigatórias")
    private String promptKeywords;

    // --- Métodos Manuais ---
    public String getPromptKeywords() {
        return promptKeywords;
    }

    public void setPromptKeywords(String promptKeywords) {
        this.promptKeywords = promptKeywords;
    }
}