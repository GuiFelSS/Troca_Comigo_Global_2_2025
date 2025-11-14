package br.com.fiap.globalSolution.Entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import br.com.fiap.globalSolution.Enum.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "TB_USUARIOS")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password; // Hash

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private Role role = Role.USER;

    private String bio;
    private String avatarUrl;

    @Column(nullable = false)
    private Double timeCredits = 10.0; // Padrão: 10h

    private Integer totalSessionsGiven = 0;
    private Integer totalSessionsTaken = 0;
    private Double averageRating = 0.0;

    private String location;
    private String timezone;
    private String linkedinUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    // ========= Relacionamentos =========
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HabilidadeEntity> habilidades;
    @OneToMany(mappedBy = "mentor")
    private List<SessoesEntity> sessoesComoMentor;
    @OneToMany(mappedBy = "mentorado")
    private List<SessoesEntity> sessoesComoMentorado;
    @OneToMany(mappedBy = "remetente")
    private List<TransferenciaEntity> transferenciasEnviadas;
    @OneToMany(mappedBy = "destinatario")
    private List<TransferenciaEntity> transferenciasRecebidas;
    @OneToMany(mappedBy = "avaliador")
    private List<AvaliacaoEntity> avaliacoesFeitas;
    @OneToMany(mappedBy = "avaliado")
    private List<AvaliacaoEntity> avaliacoesRecebidas;


    // ========= MÉTODOS MANUAIS (Getters/Setters necessários) =========

    public String getId() {
        return id;
    }

    // --- O MÉTODO QUE FALTAVA PARA O TESTE ---
    public void setId(String id) {
        this.id = id;
    }
    // ----------------------------------------

    public String getFullName() {
        return fullName;
    }

    public Double getTimeCredits() {
        return timeCredits;
    }

    public Integer getTotalSessionsGiven() {
        return totalSessionsGiven;
    }

    public void setTotalSessionsGiven(Integer totalSessionsGiven) {
        this.totalSessionsGiven = totalSessionsGiven;
    }

    public Integer getTotalSessionsTaken() {
        return totalSessionsTaken;
    }

    public void setTotalSessionsTaken(Integer totalSessionsTaken) {
        this.totalSessionsTaken = totalSessionsTaken;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    // --- Setters ---
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setTimeCredits(Double timeCredits) {
        this.timeCredits = timeCredits;
    }

    public List<HabilidadeEntity> getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(List<HabilidadeEntity> habilidades) {
        this.habilidades = habilidades;
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


    // ========= MÉTODOS DO UserDetails =========
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- BUILDER MANUAL ---
    public static UsuarioEntityBuilder builder() {
        return new UsuarioEntityBuilder();
    }

    public static class UsuarioEntityBuilder {
        private String fullName;
        private String email;
        private String password;
        private Role role;
        private Double timeCredits;

        public UsuarioEntityBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UsuarioEntityBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UsuarioEntityBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UsuarioEntityBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UsuarioEntityBuilder timeCredits(Double timeCredits) {
            this.timeCredits = timeCredits;
            return this;
        }

        public UsuarioEntity build() {
            UsuarioEntity user = new UsuarioEntity();
            user.setFullName(this.fullName);
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setRole(this.role);
            user.setTimeCredits(this.timeCredits);
            return user;
        }
    }
}