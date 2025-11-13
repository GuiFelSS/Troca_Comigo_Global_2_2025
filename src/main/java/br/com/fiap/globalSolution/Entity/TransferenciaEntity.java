package br.com.fiap.globalSolution.Entity;

import br.com.fiap.globalSolution.Enum.TransactionStatus;
import br.com.fiap.globalSolution.Enum.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
// import lombok.Getter; // Removido
// import lombok.Setter; // Removido
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_TRANSFERENCIAS")
@EntityListeners(AuditingEntityListener.class)
// @Getter // Removido
// @Setter // Removido
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sessao_id") // Pode ser nulo (ex: bônus)
    private SessoesEntity sessao;

    @ManyToOne
    @JoinColumn(name = "remetente_id", nullable = false)
    private UsuarioEntity remetente; // fromUser

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)
    private UsuarioEntity destinatario; // toUser

    @Column(nullable = false)
    private Double credits;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.PENDENTE;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    // --- GETTERS E SETTERS MANUAIS ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SessoesEntity getSessao() {
        return sessao;
    }

    public void setSessao(SessoesEntity sessao) {
        this.sessao = sessao;
    }

    public UsuarioEntity getRemetente() {
        return remetente;
    }

    public void setRemetente(UsuarioEntity remetente) {
        this.remetente = remetente;
    }

    public UsuarioEntity getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(UsuarioEntity destinatario) {
        this.destinatario = destinatario;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}