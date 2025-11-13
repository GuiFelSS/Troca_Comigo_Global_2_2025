package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Dto.AvaliacaoDto;
import br.com.fiap.globalSolution.Entity.AvaliacaoEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    /**
     * Endpoint para CRIAR uma nova avaliação.
     * O usuário logado (avaliador) avalia o outro participante da sessão.
     * O app mobile fará um POST para /api/avaliacoes
     */
    @PostMapping
    public ResponseEntity<AvaliacaoEntity> criarAvaliacao(
            @Valid @RequestBody AvaliacaoDto dto,
            @AuthenticationPrincipal UsuarioEntity usuarioLogado // Pega o avaliador
    ) {
        AvaliacaoEntity avaliacao = avaliacaoService.criarAvaliacao(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    /**
     * Endpoint para LISTAR todas as avaliações recebidas por um usuário.
     * Útil para ver o perfil de um mentor.
     * O app mobile fará um GET para /api/avaliacoes/user/{id_do_usuario}
     */
    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<List<AvaliacaoEntity>> getAvaliacoesDoUsuario(
            @PathVariable String usuarioId
    ) {
        List<AvaliacaoEntity> avaliacoes = avaliacaoService.findAvaliacoesDoUsuario(usuarioId);
        return ResponseEntity.ok(avaliacoes);
    }
}