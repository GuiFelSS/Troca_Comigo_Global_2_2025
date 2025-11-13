package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Dto.SessoesDto;
import br.com.fiap.globalSolution.Entity.SessoesEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Service.SessoesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessoes")
public class SessoesController {

    @Autowired
    private SessoesService sessoesService;

    /**
     * Endpoint para AGENDAR uma nova sessão.
     * O app mobile fará um POST para /api/sessoes
     */
    @PostMapping
    public ResponseEntity<SessoesEntity> agendarSessao(
            @Valid @RequestBody SessoesDto dto,
            @AuthenticationPrincipal UsuarioEntity usuarioLogado // Pega o mentorado (usuário logado)
    ) {
        SessoesEntity sessaoAgendada = sessoesService.agendarSessao(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoAgendada);
    }

    /**
     * Endpoint para LISTAR as sessões do usuário logado (como mentor e mentorado).
     * O app mobile fará um GET para /api/sessoes/me
     */
    @GetMapping("/me")
    public ResponseEntity<List<SessoesEntity>> getMinhasSessoes(
            @AuthenticationPrincipal UsuarioEntity usuarioLogado
    ) {
        List<SessoesEntity> sessoes = sessoesService.findMinhasSessoes(usuarioLogado);
        return ResponseEntity.ok(sessoes);
    }

    /**
     * Endpoint para CANCELAR uma sessão.
     * O app mobile fará um PATCH para /api/sessoes/{id}/cancelar
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarSessao(
            @PathVariable String id,
            @AuthenticationPrincipal UsuarioEntity usuarioLogado
    ) {
        sessoesService.cancelarSessao(id, usuarioLogado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> completarSessao(
            @PathVariable String id,
            @AuthenticationPrincipal UsuarioEntity usuarioLogado // Pega o mentor (usuário logado)
    ) {
        sessoesService.completarSessao(id, usuarioLogado);
        return ResponseEntity.ok().build(); // Retorna 200 OK
    }
}