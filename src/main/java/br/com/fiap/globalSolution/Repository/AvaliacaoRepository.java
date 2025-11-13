package br.com.fiap.globalSolution.Repository;

import br.com.fiap.globalSolution.Entity.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, String> {

    List<AvaliacaoEntity> findByAvaliadoId(String avaliadoId);

    Optional<AvaliacaoEntity> findBySessaoIdAndAvaliadorId(String sessaoId, String avaliadorId);
}