package br.com.fiap.globalSolution.Dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SessoesDto {

    @NotBlank(message = "O ID da habilidade é obrigatório")
    private String habilidadeId;

    @NotBlank(message = "O ID do mentor é obrigatório")
    private String mentorId;

    @NotNull(message = "A data de agendamento é obrigatória")
    @Future(message = "A data de agendamento deve ser no futuro")
    private LocalDateTime scheduledDate;

    private String notes; // Notas opcionais

    // --- Getters e Setters Manuais ---

    public String getHabilidadeId() {
        return habilidadeId;
    }

    public void setHabilidadeId(String habilidadeId) {
        this.habilidadeId = habilidadeId;
    }

    public String getMentorId() {
        return mentorId;
    }

    public void setMentorId(String mentorId) {
        this.mentorId = mentorId;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}