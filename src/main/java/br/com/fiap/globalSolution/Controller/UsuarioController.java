package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Dto.UsuarioDto;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para LISTAR todos os usuários (mentores) de forma paginada.
     * O app mobile fará um GET para /api/users?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<UsuarioEntity>> getAllUsers(
            @PageableDefault(size = 10, sort = "fullName") Pageable pageable
    ) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    /**
     * Endpoint para buscar o PERFIL PÚBLICO de um usuário específico.
     * O app mobile fará um GET para /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    /**
     * Endpoint para buscar o PERFIL do PRÓPRIO usuário logado.
     * O app mobile fará um GET para /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioEntity> getMeuPerfil(
            @AuthenticationPrincipal UsuarioEntity usuarioLogado
    ) {
        // O Spring Security já nos entrega a entidade do usuário logado
        return ResponseEntity.ok(usuarioLogado);
    }

    /**
     * Endpoint para ATUALIZAR o PERFIL do usuário logado.
     * O app mobile fará um PUT para /api/users/me
     */
    @PutMapping("/me")
    public ResponseEntity<UsuarioEntity> updateMeuPerfil(
            @AuthenticationPrincipal UsuarioEntity usuarioLogado,
            @Valid @RequestBody UsuarioDto dto
    ) {
        UsuarioEntity usuarioAtualizado = usuarioService.updateProfile(usuarioLogado, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}