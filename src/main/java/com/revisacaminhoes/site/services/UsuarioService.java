package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.Role;
import com.revisacaminhoes.site.entities.Usuario;
import com.revisacaminhoes.site.repositories.UsuarioRepository;
import com.revisacaminhoes.site.requestdto.AtualizarSenhaRequestDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // usado pelo Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().name()) // ADMIN / USER
                .build();
    }

    // cadastro simples (bootstrap)
    public Usuario criarUsuario(String username, String password, String role) {
        usuarioRepository.findByUsername(username).ifPresent(x -> {
            throw new RuntimeException("Usuário já existe");
        });

        Usuario novo = Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role.toUpperCase()))
                .build();

        return usuarioRepository.save(novo);
    }

    // alterar senha do usuário logado
    public void atualizarSenha(AtualizarSenhaRequestDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}
