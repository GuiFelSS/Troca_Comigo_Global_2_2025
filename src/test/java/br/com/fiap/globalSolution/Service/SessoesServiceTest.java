package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.SessoesDto;
import br.com.fiap.globalSolution.Entity.HabilidadeEntity;
import br.com.fiap.globalSolution.Entity.SessoesEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Repository.HabilidadeRepository;
import br.com.fiap.globalSolution.Repository.SessoesRepository;
import br.com.fiap.globalSolution.Repository.TransferenciaRepository;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Usa Mockito para não precisar subir o Spring inteiro
class SessoesServiceTest {

    @InjectMocks
    private SessoesService sessoesService;

    @Mock
    private SessoesRepository sessoesRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private HabilidadeRepository habilidadeRepository;
    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Test
    @DisplayName("Deve agendar sessão com sucesso quando tudo estiver válido")
    void agendarSessao_CenarioSucesso() {
        // --- CENÁRIO (GIVEN) ---
        UsuarioEntity mentor = new UsuarioEntity();
        mentor.setId("mentor-123");

        UsuarioEntity mentorado = new UsuarioEntity();
        mentorado.setId("aluno-456");
        mentorado.setTimeCredits(10.0); // Tem crédito suficiente

        HabilidadeEntity habilidade = new HabilidadeEntity();
        habilidade.setId("java-skill");
        habilidade.setUsuario(mentor); // Habilidade pertence ao mentor
        habilidade.setIsOffering(true); // Está sendo ofertada
        habilidade.setName("Java Avançado");

        SessoesDto dto = new SessoesDto();
        dto.setMentorId("mentor-123");
        dto.setHabilidadeId("java-skill");
        dto.setScheduledDate(LocalDateTime.now().plusDays(1));

        // Mock do comportamento dos repositórios
        when(usuarioRepository.findById("mentor-123")).thenReturn(Optional.of(mentor));
        when(habilidadeRepository.findById("java-skill")).thenReturn(Optional.of(habilidade));
        when(sessoesRepository.save(any(SessoesEntity.class))).thenAnswer(i -> i.getArgument(0)); // Retorna o próprio objeto salvo

        // --- AÇÃO (WHEN) ---
        SessoesEntity resultado = sessoesService.agendarSessao(dto, mentorado);

        // --- VERIFICAÇÃO (THEN) ---
        assertNotNull(resultado);
        assertEquals("Java Avançado", resultado.getSkillName());
        assertEquals(9.0, mentorado.getTimeCredits()); // Verificando se debitou o crédito

        // Verifica se salvou a transação
        verify(transferenciaRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Não deve agendar se o mentorado não tiver créditos")
    void agendarSessao_SemCredito() {
        // --- CENÁRIO ---
        UsuarioEntity mentor = new UsuarioEntity();
        mentor.setId("mentor-123");

        UsuarioEntity mentorado = new UsuarioEntity();
        mentorado.setId("aluno-456");
        mentorado.setTimeCredits(0.0); // SEM CRÉDITO!

        HabilidadeEntity habilidade = new HabilidadeEntity();
        habilidade.setId("java-skill");
        habilidade.setUsuario(mentor);
        habilidade.setIsOffering(true);

        SessoesDto dto = new SessoesDto();
        dto.setMentorId("mentor-123");
        dto.setHabilidadeId("java-skill");
        dto.setScheduledDate(LocalDateTime.now().plusDays(1));

        when(usuarioRepository.findById("mentor-123")).thenReturn(Optional.of(mentor));
        when(habilidadeRepository.findById("java-skill")).thenReturn(Optional.of(habilidade));

        // --- AÇÃO E VERIFICAÇÃO ---
        assertThrows(ResponseStatusException.class, () -> {
            sessoesService.agendarSessao(dto, mentorado);
        });

        // Garante que NÃO salvou sessão nem debitou crédito
        verify(sessoesRepository, never()).save(any());
    }
}