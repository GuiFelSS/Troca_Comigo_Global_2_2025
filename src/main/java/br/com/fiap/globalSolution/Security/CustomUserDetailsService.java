package br.com.fiap.globalSolution.Security;

import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired; // <-- IMPORTAR
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Injeta o repositório diretamente aqui
    @Autowired
    private UsuarioRepository usuarioRepository;

    /* Removemos o construtor manual.
       O @Autowired acima cuida de tudo.
    */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}