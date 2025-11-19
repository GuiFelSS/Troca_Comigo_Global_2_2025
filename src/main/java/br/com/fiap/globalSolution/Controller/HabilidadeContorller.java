package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Dto.HabilidadeDto;
import br.com.fiap.globalSolution.Entity.HabilidadeEntity;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Service.HabilidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habilidades")
public class HabilidadeContorller {

    @Autowired
    private HabilidadeService habilidadeService;

    /**
     * Endpoint para LISTAR as habilidades do PRÓPRIO usuário logado.
     * O app mobile fará um GET para /api/habilidades/me
     */
    @GetMapping("/me")
    public ResponseEntity<List<HabilidadeEntity>> getMinhasHabilidades(
            @AuthenticationPrincipal UsuarioEntity usuario // Pega o usuário logado
    ) {
        return ResponseEntity.ok(habilidadeService.findByUsuario(usuario));
    }

    /**
     * Endpoint para CRIAR uma nova habilidade para o usuário logado.
     * O app mobile fará um POST para /api/habilidades
     */
    @PostMapping
    public ResponseEntity<HabilidadeEntity> createHabilidade(
            @Valid @RequestBody HabilidadeDto dto,
            @AuthenticationPrincipal UsuarioEntity usuario // Pega o usuário logado
    ) {
        HabilidadeEntity novaHabilidade = habilidadeService.create(dto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaHabilidade);
    }

    /**
     * Endpoint para ATUALIZAR uma habilidade existente.
     * O app mobile fará um PUT para /api/habilidades/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<HabilidadeEntity> updateHabilidade(
            @PathVariable String id,
            @Valid @RequestBody HabilidadeDto dto,
            @AuthenticationPrincipal UsuarioEntity usuario
    ) {
        HabilidadeEntity habilidadeAtualizada = habilidadeService.update(id, dto, usuario);
        return ResponseEntity.ok(habilidadeAtualizada);
    }

    /**
     * Endpoint para DELETAR uma habilidade do usuário logado.
     * O app mobile fará um DELETE para /api/habilidades/{id_da_habilidade}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabilidade(
            @PathVariable String id,
            @AuthenticationPrincipal UsuarioEntity usuario // Pega o usuário logado
    ) {
        habilidadeService.delete(id, usuario);
        return ResponseEntity.noContent().build();
    }
}