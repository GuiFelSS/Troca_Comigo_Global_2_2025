package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Dto.TransferenciaDto; // <-- Importar o DTO
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciasController {

    @Autowired
    private TransferenciaService transferenciaService;

    /**
     * Endpoint para LISTAR o extrato de transações (créditos) do usuário logado.
     * Agora retorna um DTO seguro.
     */
    @GetMapping("/me")
    public ResponseEntity<Page<TransferenciaDto>> getMeuExtrato( // <-- TIPO DE RETORNO MUDOU
                                                                 @AuthenticationPrincipal UsuarioEntity usuarioLogado,
                                                                 @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // O serviço agora retorna o DTO diretamente
        Page<TransferenciaDto> extrato = transferenciaService.findExtratoDoUsuario(usuarioLogado, pageable);
        return ResponseEntity.ok(extrato);
    }
}