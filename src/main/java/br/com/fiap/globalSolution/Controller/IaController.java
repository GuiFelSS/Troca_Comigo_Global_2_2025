package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Dto.IaRequestDto;
import br.com.fiap.globalSolution.Service.IaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ia") // URL base para a IA
public class IaController {

    @Autowired
    private IaService iaService;

    /**
     * Endpoint para gerar uma Bio de perfil.
     * O app mobile fará um POST para /api/ia/gerar-bio
     * enviando o token JWT no header "Authorization".
     */
    @PostMapping("/gerar-bio")
    public ResponseEntity<String> gerarBio(
            @Valid @RequestBody IaRequestDto request
    ) {
        String bioGerada = iaService.gerarBio(request.getPromptKeywords());
        return ResponseEntity.ok(bioGerada);
    }
}