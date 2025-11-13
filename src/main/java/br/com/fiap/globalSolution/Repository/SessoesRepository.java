package br.com.fiap.globalSolution.Repository;

import br.com.fiap.globalSolution.Entity.SessoesEntity;
import br.com.fiap.globalSolution.Enum.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessoesRepository extends JpaRepository<SessoesEntity, String> {

    List<SessoesEntity> findByMentorIdOrMentoradoId(String mentorId, String mentoradoId);

    List<SessoesEntity> findByStatus(SessionStatus status);

    List<SessoesEntity> findByMentorIdAndStatus(String mentorId, SessionStatus status);
}