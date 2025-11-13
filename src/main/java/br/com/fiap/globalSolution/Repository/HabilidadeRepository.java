package br.com.fiap.globalSolution.Repository;

import br.com.fiap.globalSolution.Entity.HabilidadeEntity;
import br.com.fiap.globalSolution.Enum.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabilidadeRepository extends JpaRepository<HabilidadeEntity, String> {

    List<HabilidadeEntity> findByUsuarioId(String usuarioId);

    List<HabilidadeEntity> findByIsOfferingTrue();

    List<HabilidadeEntity> findByIsSeekingTrue();

    List<HabilidadeEntity> findByCategory(Category category);
}