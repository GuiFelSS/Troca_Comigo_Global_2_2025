package br.com.fiap.globalSolution.Dto;

import br.com.fiap.globalSolution.Enum.TransactionStatus;
import br.com.fiap.globalSolution.Enum.TransactionType;
import java.time.LocalDateTime;

// Este DTO é usado para ENVIAR dados para o app (Resposta)
public class TransferenciaDto {

    private String id;
    private String description;
    private Double credits;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime createdDate;

    // Nomes em vez de IDs para proteger os dados
    private String remetenteName;
    private String destinatarioName;

    // --- Getters e Setters Manuais ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemetenteName() {
        return remetenteName;
    }

    public void setRemetenteName(String remetenteName) {
        this.remetenteName = remetenteName;
    }

    public String getDestinatarioName() {
        return destinatarioName;
    }

    public void setDestinatarioName(String destinatarioName) {
        this.destinatarioName = destinatarioName;
    }
}