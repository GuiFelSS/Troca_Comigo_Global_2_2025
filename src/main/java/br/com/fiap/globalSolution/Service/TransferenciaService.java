package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.TransferenciaDto; // <-- Importar o DTO
import br.com.fiap.globalSolution.Entity.TransferenciaEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Repository.TransferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    /**
     * Busca o histórico de transações (extrato) e converte para DTO.
     */
    public Page<TransferenciaDto> findExtratoDoUsuario(UsuarioEntity usuario, Pageable pageable) {
        // 1. Busca a página de Entidades do repositório
        Page<TransferenciaEntity> entityPage = transferenciaRepository.findByRemetenteIdOrDestinatarioId(
                usuario.getId(),
                usuario.getId(),
                pageable
        );

        // 2. Converte a página de Entidades para uma página de DTOs
        // O .map() do Page faz a conversão item por item
        return entityPage.map(this::convertToDto);
    }

    /**
     * Método auxiliar privado para converter uma Entidade em um DTO.
     */
    private TransferenciaDto convertToDto(TransferenciaEntity entity) {
        TransferenciaDto dto = new TransferenciaDto();
        dto.setId(entity.getId());
        dto.setCredits(entity.getCredits());
        dto.setStatus(entity.getStatus());
        dto.setType(entity.getType());
        dto.setCreatedDate(entity.getCreatedDate());

        // Se a descrição estiver vazia, cria uma padrão
        if (entity.getDescription() == null || entity.getDescription().isBlank()) {
            dto.setDescription(entity.getType().toString()); // Ex: "PAGAMENTO_SESSAO"
        } else {
            dto.setDescription(entity.getDescription());
        }

        // Pega os nomes para o extrato
        dto.setRemetenteName(entity.getRemetente().getFullName());
        dto.setDestinatarioName(entity.getDestinatario().getFullName());

        return dto;
    }
}