package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.HabilidadeDto;
import br.com.fiap.globalSolution.Entity.HabilidadeEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Repository.HabilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HabilidadeService {

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    // Método para LISTAR habilidades de um usuário
    public List<HabilidadeEntity> findByUsuario(UsuarioEntity usuario) {
        return habilidadeRepository.findByUsuarioId(usuario.getId());
    }

    // Método para CRIAR uma nova habilidade para o usuário logado
    @Transactional
    public HabilidadeEntity create(HabilidadeDto dto, UsuarioEntity usuario) {
        HabilidadeEntity habilidade = new HabilidadeEntity();
        habilidade.setName(dto.getName());
        habilidade.setCategory(dto.getCategory());
        habilidade.setDescription(dto.getDescription());
        habilidade.setLevel(dto.getLevel());
        habilidade.setIsOffering(dto.getIsOffering());
        habilidade.setIsSeeking(dto.getIsSeeking());
        habilidade.setHourlyRate(dto.getHourlyRate());

        habilidade.setUsuario(usuario); // Associa ao usuário logado
        habilidade.setCreatedDate(LocalDateTime.now());

        return habilidadeRepository.save(habilidade);
    }

    @Transactional
    public HabilidadeEntity update(String id, HabilidadeDto dto, UsuarioEntity usuario) {
        // 1. Busca a habilidade
        HabilidadeEntity habilidade = habilidadeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habilidade não encontrada"));

        // 2. Verifica se pertence ao usuário
        if (!habilidade.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar esta habilidade");
        }

        // 3. Atualiza os dados
        habilidade.setName(dto.getName());
        habilidade.setCategory(dto.getCategory());
        habilidade.setDescription(dto.getDescription());
        habilidade.setLevel(dto.getLevel());
        habilidade.setIsOffering(dto.getIsOffering());
        habilidade.setIsSeeking(dto.getIsSeeking());
        habilidade.setHourlyRate(dto.getHourlyRate());

        // Nota: Não atualizamos createdDate nem usuario

        return habilidadeRepository.save(habilidade);
    }

    // Método para DELETAR uma habilidade
    @Transactional
    public void delete(String habilidadeId, UsuarioEntity usuario) {
        // 1. Busca a habilidade
        HabilidadeEntity habilidade = habilidadeRepository.findById(habilidadeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habilidade não encontrada"));

        // 2. Verifica se a habilidade pertence ao usuário que está pedindo para deletar
        if (!habilidade.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar esta habilidade");
        }

        // 3. Deleta
        habilidadeRepository.delete(habilidade);
    }
}