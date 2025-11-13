package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.SessoesDto;
import br.com.fiap.globalSolution.Entity.HabilidadeEntity;
import br.com.fiap.globalSolution.Entity.SessoesEntity;
import br.com.fiap.globalSolution.Entity.TransferenciaEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Enum.SessionStatus;
import br.com.fiap.globalSolution.Enum.TransactionStatus;
import br.com.fiap.globalSolution.Enum.TransactionType;
import br.com.fiap.globalSolution.Repository.HabilidadeRepository;
import br.com.fiap.globalSolution.Repository.SessoesRepository;
import br.com.fiap.globalSolution.Repository.TransferenciaRepository;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessoesService {

    @Autowired
    private SessoesRepository sessoesRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    /**
     * Lista todas as sessões (como mentor e mentorado) do usuário logado.
     */
    public List<SessoesEntity> findMinhasSessoes(UsuarioEntity usuario) {
        return sessoesRepository.findByMentorIdOrMentoradoId(usuario.getId(), usuario.getId());
    }

    /**
     * Lógica principal: Agenda uma nova sessão.
     */
    @Transactional
    public SessoesEntity agendarSessao(SessoesDto dto, UsuarioEntity mentorado) {

        // 1. Validar se o usuário não está agendando consigo mesmo
        if (mentorado.getId().equals(dto.getMentorId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode agendar uma sessão consigo mesmo.");
        }

        // 2. Buscar o mentor e a habilidade
        UsuarioEntity mentor = usuarioRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor não encontrado"));

        HabilidadeEntity habilidade = habilidadeRepository.findById(dto.getHabilidadeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habilidade não encontrada"));

        // 3. Validar a lógica de negócio
        if (!habilidade.getUsuario().getId().equals(mentor.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta habilidade não pertence ao mentor selecionado.");
        }
        if (!habilidade.getIsOffering()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O mentor não está oferecendo esta habilidade no momento.");
        }

        // 4. Calcular custo e verificar créditos
        // (Usaremos 1h = 1 crédito por padrão, mas você pode usar o hourlyRate da habilidade)
        Double custoCreditos = 1.0;
        if (mentorado.getTimeCredits() < custoCreditos) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Créditos insuficientes para agendar esta sessão.");
        }

        // 5. Debitar créditos do mentorado (o save real é no final)
        mentorado.setTimeCredits(mentorado.getTimeCredits() - custoCreditos);

        // 6. Criar a entidade Sessao
        SessoesEntity sessao = new SessoesEntity();
        sessao.setMentorado(mentorado);
        sessao.setMentor(mentor);
        sessao.setHabilidade(habilidade);
        sessao.setSkillName(habilidade.getName()); // Salva o nome da habilidade
        sessao.setScheduledDate(dto.getScheduledDate());
        sessao.setCreditsValue(custoCreditos);
        sessao.setStatus(SessionStatus.AGENDADA);
        sessao.setNotes(dto.getNotes());
        sessao.setCreatedDate(LocalDateTime.now());

        SessoesEntity sessaoSalva = sessoesRepository.save(sessao);

        // 7. Criar a Transação (Transferência) PENDENTE
        TransferenciaEntity transferencia = new TransferenciaEntity();
        transferencia.setSessao(sessaoSalva);
        transferencia.setRemetente(mentorado);
        transferencia.setDestinatario(mentor);
        transferencia.setCredits(custoCreditos);
        transferencia.setType(TransactionType.PAGAMENTO_SESSAO);
        transferencia.setStatus(TransactionStatus.PENDENTE); // Fica pendente até a sessão ser concluída
        transferencia.setCreatedDate(LocalDateTime.now());

        transferenciaRepository.save(transferencia);
        usuarioRepository.save(mentorado); // Salva o usuário com os créditos debitados

        return sessaoSalva;
    }

    /**
     * Cancela uma sessão.
     */
    @Transactional
    public void cancelarSessao(String sessaoId, UsuarioEntity usuario) {
        SessoesEntity sessao = sessoesRepository.findById(sessaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada"));

        // 1. Verifica se o usuário é o mentor OU o mentorado
        boolean isMentor = sessao.getMentor().getId().equals(usuario.getId());
        boolean isMentorado = sessao.getMentorado().getId().equals(usuario.getId());

        if (!isMentor && !isMentorado) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para cancelar esta sessão.");
        }

        // 2. Não pode cancelar sessão já concluída
        if (sessao.getStatus() == SessionStatus.CONCLUIDA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar uma sessão já concluída.");
        }

        // 3. Atualiza o status da sessão
        sessao.setStatus(SessionStatus.CANCELADA);
        sessoesRepository.save(sessao);

        // 4. Encontra a transação pendente e estorna
        TransferenciaEntity transferencia = transferenciaRepository.findBySessaoId(sessao.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transação da sessão não encontrada"));

        if (transferencia.getStatus() == TransactionStatus.PENDENTE) {
            transferencia.setStatus(TransactionStatus.ESTORNADA);
            transferenciaRepository.save(transferencia);

            // 5. Devolve os créditos ao mentorado
            UsuarioEntity mentorado = sessao.getMentorado();
            mentorado.setTimeCredits(mentorado.getTimeCredits() + sessao.getCreditsValue());
            usuarioRepository.save(mentorado);
        }
    }

    @Transactional
    public void completarSessao(String sessaoId, UsuarioEntity usuarioMentor) {
        SessoesEntity sessao = sessoesRepository.findById(sessaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada"));

        // 1. Verifica se o usuário logado é o MENTOR da sessão
        if (!sessao.getMentor().getId().equals(usuarioMentor.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o mentor pode completar a sessão.");
        }

        // 2. Verifica se a sessão está com status 'AGENDADA'
        if (sessao.getStatus() != SessionStatus.AGENDADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta sessão não pode ser concluída (status: " + sessao.getStatus() + ").");
        }

        // 3. Muda o status da sessão
        sessao.setStatus(SessionStatus.CONCLUIDA);

        // 4. Encontra a transação pendente
        TransferenciaEntity transferencia = transferenciaRepository.findBySessaoId(sessao.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transação da sessão não encontrada"));

        // 5. Libera a transação (Pendente -> Concluída)
        transferencia.setStatus(TransactionStatus.CONCLUIDA);

        // 6. Atualiza os créditos e contadores do Mentor
        UsuarioEntity mentor = sessao.getMentor();
        mentor.setTimeCredits(mentor.getTimeCredits() + sessao.getCreditsValue());
        mentor.setTotalSessionsGiven(mentor.getTotalSessionsGiven() + 1);

        // 7. Atualiza o contador do Mentorado
        UsuarioEntity mentorado = sessao.getMentorado();
        mentorado.setTotalSessionsTaken(mentorado.getTotalSessionsTaken() + 1);

        // 8. Salva todas as entidades modificadas
        sessoesRepository.save(sessao);
        transferenciaRepository.save(transferencia);
        usuarioRepository.save(mentor);
        usuarioRepository.save(mentorado);
    }
}