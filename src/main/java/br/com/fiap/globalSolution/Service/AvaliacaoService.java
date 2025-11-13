package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.AvaliacaoDto;
import br.com.fiap.globalSolution.Entity.AvaliacaoEntity;
import br.com.fiap.globalSolution.Entity.SessoesEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Enum.SessionStatus;
import br.com.fiap.globalSolution.Repository.AvaliacaoRepository;
import br.com.fiap.globalSolution.Repository.SessoesRepository;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private SessoesRepository sessoesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Lista todas as avaliações recebidas por um usuário específico.
     */
    public List<AvaliacaoEntity> findAvaliacoesDoUsuario(String usuarioId) {
        return avaliacaoRepository.findByAvaliadoId(usuarioId);
    }

    /**
     * Cria uma nova avaliação para uma sessão.
     */
    @Transactional
    public AvaliacaoEntity criarAvaliacao(AvaliacaoDto dto, UsuarioEntity avaliador) {
        SessoesEntity sessao = sessoesRepository.findById(dto.getSessaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada"));

        // 1. Verifica se a sessão foi concluída
        if (sessao.getStatus() != SessionStatus.CONCLUIDA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você só pode avaliar sessões concluídas.");
        }

        // 2. Determina quem é o avaliador e quem é o avaliado
        UsuarioEntity avaliado;
        boolean isMentorAvaliando = sessao.getMentor().getId().equals(avaliador.getId());
        boolean isMentoradoAvaliando = sessao.getMentorado().getId().equals(avaliador.getId());

        if (isMentorAvaliando) {
            avaliado = sessao.getMentorado();
        } else if (isMentoradoAvaliando) {
            avaliado = sessao.getMentor();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não participou desta sessão.");
        }

        // 3. Verifica se este usuário já avaliou esta sessão
        boolean jaAvaliou = avaliacaoRepository.findBySessaoIdAndAvaliadorId(sessao.getId(), avaliador.getId()).isPresent();
        if (jaAvaliou) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já avaliou esta sessão.");
        }

        // 4. Cria e salva a avaliação
        AvaliacaoEntity avaliacao = new AvaliacaoEntity();
        avaliacao.setSessao(sessao);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setAvaliado(avaliado);
        avaliacao.setRating(dto.getRating());
        avaliacao.setComment(dto.getComment());
        avaliacao.setCreatedDate(LocalDateTime.now());

        avaliacaoRepository.save(avaliacao);

        // 5. Recalcula a média de rating do usuário avaliado
        atualizarMediaRating(avaliado);

        return avaliacao;
    }

    /**
     * Método privado para recalcular a média de um usuário e salvar.
     */
    private void atualizarMediaRating(UsuarioEntity avaliado) {
        // 1. Busca todas as avaliações recebidas pelo usuário
        List<AvaliacaoEntity> avaliacoesRecebidas = avaliacaoRepository.findByAvaliadoId(avaliado.getId());

        if (avaliacoesRecebidas.isEmpty()) {
            avaliado.setAverageRating(0.0);
        } else {
            // 2. Calcula a média
            double soma = 0;
            for (AvaliacaoEntity review : avaliacoesRecebidas) {
                soma += review.getRating();
            }
            double media = soma / avaliacoesRecebidas.size();

            // 3. Atualiza o usuário
            avaliado.setAverageRating(media);
        }

        // 4. Salva o usuário com a nova média
        usuarioRepository.save(avaliado);
    }
}