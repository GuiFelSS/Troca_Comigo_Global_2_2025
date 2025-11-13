package br.com.fiap.globalSolution.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
// import lombok.Getter; // Removido
// import lombok.Setter; // Removido
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_AVALIACOES")
@EntityListeners(AuditingEntityListener.class)
// @Getter // Removido
// @Setter // Removido
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sessao_id", nullable = false)
    private SessoesEntity sessao;

    @ManyToOne
    @JoinColumn(name = "avaliador_id", nullable = false)
    private UsuarioEntity avaliador; // reviewer

    @ManyToOne
    @JoinColumn(name = "avaliado_id", nullable = false)
    private UsuarioEntity avaliado; // reviewed

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(length = 2000)
    private String comment;

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

    public UsuarioEntity getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(UsuarioEntity avaliador) {
        this.avaliador = avaliador;
    }

    public UsuarioEntity getAvaliado() {
        return avaliado;
    }

    public void setAvaliado(UsuarioEntity avaliado) {
        this.avaliado = avaliado;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}