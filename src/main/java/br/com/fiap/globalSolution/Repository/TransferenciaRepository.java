package br.com.fiap.globalSolution.Repository;

import br.com.fiap.globalSolution.Entity.TransferenciaEntity;
import br.com.fiap.globalSolution.Enum.TransactionStatus;
import org.springframework.data.domain.Page; // <-- Importar Page
import org.springframework.data.domain.Pageable; // <-- Importar Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferenciaRepository extends JpaRepository<TransferenciaEntity, String> {

    // Método antigo (sem paginável)
    List<TransferenciaEntity> findByRemetenteIdOrDestinatarioId(String remetenteId, String destinatarioId);

    // ---- NOVO MÉTODO (com paginável) ----
    Page<TransferenciaEntity> findByRemetenteIdOrDestinatarioId(String remetenteId, String destinatarioId, Pageable pageable);

    List<TransferenciaEntity> findByStatus(TransactionStatus status);

    // Método que adicionamos para o SessoesService
    Optional<TransferenciaEntity> findBySessaoId(String sessaoId);
}