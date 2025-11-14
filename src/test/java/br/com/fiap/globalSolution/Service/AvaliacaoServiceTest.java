package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.AvaliacaoDto;
import br.com.fiap.globalSolution.Entity.AvaliacaoEntity;
import br.com.fiap.globalSolution.Entity.SessoesEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Enum.SessionStatus;
import br.com.fiap.globalSolution.Repository.AvaliacaoRepository;
import br.com.fiap.globalSolution.Repository.SessoesRepository;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private SessoesRepository sessoesRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve criar avaliação e recalcular média do usuário avaliado")
    void criarAvaliacao_ComSucesso_DeveRecalcularMedia() {
        // --- CENÁRIO ---
        // 1. DTO (Input)
        AvaliacaoDto dto = new AvaliacaoDto();
        dto.setSessaoId("sessao-123");
        dto.setRating(5); // Nova avaliação nota 5

        // 2. Usuários
        UsuarioEntity avaliador = new UsuarioEntity(); // Mentorado
        avaliador.setId("aluno-001");

        UsuarioEntity avaliado = new UsuarioEntity(); // Mentor
        avaliado.setId("mentor-002");
        avaliado.setAverageRating(3.0); // Tinha média 3.0

        // 3. Sessão
        SessoesEntity sessao = new SessoesEntity();
        sessao.setId("sessao-123");
        sessao.setStatus(SessionStatus.CONCLUIDA); // Sessão está concluída
        sessao.setMentor(avaliado);
        sessao.setMentorado(avaliador);

        // 4. Mocks dos Repositórios
        when(sessoesRepository.findById("sessao-123")).thenReturn(Optional.of(sessao));
        when(avaliacaoRepository.findBySessaoIdAndAvaliadorId("sessao-123", "aluno-001")).thenReturn(Optional.empty()); // Não avaliou ainda

        // Mock das avaliações ANTERIORES do mentor
        AvaliacaoEntity avaliacaoAntiga = new AvaliacaoEntity();
        avaliacaoAntiga.setRating(3); // A nota antiga que deu a média 3.0

        // Mock da NOVA avaliação que será salva
        AvaliacaoEntity novaAvaliacao = new AvaliacaoEntity();
        novaAvaliacao.setRating(5); // A nova nota 5

        // Mock para o recálculo da média:
        // O service vai salvar a nova avaliação e DEPOIS buscar TODAS.
        // Simulamos que, após o save, o findByAvaliadoId retornará ambas.
        when(avaliacaoRepository.findByAvaliadoId("mentor-002")).thenReturn(List.of(avaliacaoAntiga, novaAvaliacao));

        // --- AÇÃO ---
        AvaliacaoEntity resultado = avaliacaoService.criarAvaliacao(dto, avaliador);

        // --- VERIFICAÇÃO ---

        // 1. Verifica se a avaliação foi salva
        verify(avaliacaoRepository, times(1)).save(any(AvaliacaoEntity.class));

        // 2. Captura o usuário que foi salvo
        ArgumentCaptor<UsuarioEntity> usuarioCaptor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(usuarioRepository, times(1)).save(usuarioCaptor.capture());

        // 3. Verifica se a média foi recalculada corretamente
        UsuarioEntity mentorAtualizado = usuarioCaptor.getValue();
        assertEquals(4.0, mentorAtualizado.getAverageRating()); // Média de (3 + 5) / 2 = 4.0
    }

    @Test
    @DisplayName("Não deve permitir avaliar sessão que não está concluída")
    void criarAvaliacao_SessaoNaoConcluida() {
        // --- CENÁRIO ---
        AvaliacaoDto dto = new AvaliacaoDto();
        dto.setSessaoId("sessao-agendada");

        UsuarioEntity avaliador = new UsuarioEntity();
        avaliador.setId("aluno-001");

        SessoesEntity sessao = new SessoesEntity();
        sessao.setStatus(SessionStatus.AGENDADA); // Status NÃO CONCLUÍDA

        when(sessoesRepository.findById("sessao-agendada")).thenReturn(Optional.of(sessao));

        // --- AÇÃO E VERIFICAÇÃO ---
        assertThrows(ResponseStatusException.class, () -> {
            avaliacaoService.criarAvaliacao(dto, avaliador);
        });

        // Garante que NADA foi salvo
        verify(avaliacaoRepository, never()).save(any());
        verify(usuarioRepository, never()).save(any());
    }
}