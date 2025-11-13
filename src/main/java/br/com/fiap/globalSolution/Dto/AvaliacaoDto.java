package br.com.fiap.globalSolution.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AvaliacaoDto {

    @NotBlank(message = "O ID da sessão é obrigatório")
    private String sessaoId;

    @NotNull(message = "A nota (rating) é obrigatória")
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment; // Comentário opcional

    // --- Getters e Setters Manuais ---

    public String getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(String sessaoId) {
        this.sessaoId = sessaoId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}