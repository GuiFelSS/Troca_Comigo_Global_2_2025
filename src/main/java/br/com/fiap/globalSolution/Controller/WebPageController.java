package br.com.fiap.globalSolution.Controller;

import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <-- Note: @Controller, e não @RestController
public class WebPageController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Este método vai carregar a página index.html
     * e injetar a lista de usuários nela.
     */
    @GetMapping("/")
    public String getHomePage(Model model) {
        // Busca a primeira página de 10 usuários para exibir
        Page<UsuarioEntity> usuariosPage = usuarioService.findAll(PageRequest.of(0, 10));
        model.addAttribute("usuarios", usuariosPage.getContent());

        return "index"; // Retorna o nome do arquivo: "index.html"
    }
}