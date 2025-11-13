package br.com.fiap.globalSolution.Service;

import br.com.fiap.globalSolution.Dto.UsuarioDto;
import br.com.fiap.globalSolution.Entity.UsuarioEntity;
import br.com.fiap.globalSolution.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Busca um usuário pelo ID.
     * A anotação @Cacheable("usuarios") guarda o resultado em um cache.
     * (Já implementado na Prioridade 4)
     */
    @Cacheable("usuarios")
    public UsuarioEntity findById(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + id));
    }

    /**
     * Lista todos os usuários de forma paginada.
     */
    public Page<UsuarioEntity> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    /**
     * Atualiza o perfil do usuário logado.
     * @CacheEvict limpa o cache "usuarios" para este ID, forçando
     * a próxima chamada a `findById` a buscar os dados atualizados do banco.
     */
    @Transactional
    @CacheEvict(value = "usuarios", key = "#usuario.id")
    public UsuarioEntity updateProfile(UsuarioEntity usuario, UsuarioDto dto) {
        // Atualiza apenas os campos permitidos
        usuario.setFullName(dto.getFullName());
        usuario.setBio(dto.getBio());
        usuario.setAvatarUrl(dto.getAvatarUrl());
        usuario.setLocation(dto.getLocation());
        usuario.setTimezone(dto.getTimezone());
        usuario.setLinkedinUrl(dto.getLinkedinUrl());

        return usuarioRepository.save(usuario);
    }
}