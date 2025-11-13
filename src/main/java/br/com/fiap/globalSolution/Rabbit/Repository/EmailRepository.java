package br.com.fiap.globalSolution.Rabbit.Repository;

import br.com.fiap.globalSolution.Rabbit.Entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<EmailEntity, UUID> {
}
