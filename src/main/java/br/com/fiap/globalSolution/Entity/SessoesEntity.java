package br.com.fiap.globalSolution.Entity;

import br.com.fiap.globalSolution.Enum.SessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
// import lombok.Getter; // Removido
// import lombok.Setter; // Removido
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_SESSOES")
@EntityListeners(AuditingEntityListener.class)
// @Getter // Removido
// @Setter // Removido
@NoArgsConstructor
@AllArgsConstructor
public class SessoesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private UsuarioEntity mentor;

    @ManyToOne
    @JoinColumn(name = "mentorado_id", nullable = false)
    private UsuarioEntity mentorado;

    @ManyToOne
    @JoinColumn(name = "habilidade_id", nullable = false)
    private HabilidadeEntity habilidade;

    private String skillName; // Para guardar o nome da habilidade na época

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    @Column(nullable = false)
    private Double durationHours = 1.0;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.AGENDADA;

    private String meetingLink;

    @Column(length = 2000)
    private String notes;

    private Double creditsValue; // Valor em créditos da sessão

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    // Relacionamentos
    @OneToMany(mappedBy = "sessao")
    private List<TransferenciaEntity> transferencias;

    @OneToMany(mappedBy = "sessao")
    private List<AvaliacaoEntity> avaliacoes;

    // --- GETTERS E SETTERS MANUAIS ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UsuarioEntity getMentor() {
        return mentor;
    }

    public void setMentor(UsuarioEntity mentor) {
        this.mentor = mentor;
    }

    public UsuarioEntity getMentorado() {
        return mentorado;
    }

    public void setMentorado(UsuarioEntity mentorado) {
        this.mentorado = mentorado;
    }

    public HabilidadeEntity getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(HabilidadeEntity habilidade) {
        this.habilidade = habilidade;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Double durationHours) {
        this.durationHours = durationHours;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getCreditsValue() {
        return creditsValue;
    }

    public void setCreditsValue(Double creditsValue) {
        this.creditsValue = creditsValue;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<TransferenciaEntity> getTransferencias() {
        return transferencias;
    }

    public void setTransferencias(List<TransferenciaEntity> transferencias) {
        this.transferencias = transferencias;
    }

    public List<AvaliacaoEntity> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<AvaliacaoEntity> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
}