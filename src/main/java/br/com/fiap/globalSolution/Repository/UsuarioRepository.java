package br.com.fiap.globalSolution.Repository;

import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {

    // Necessário para o Spring Security (CustomUserDetailsService)
    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}