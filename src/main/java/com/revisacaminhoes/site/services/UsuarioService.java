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
import com.revisacaminhoes.site.responsedto.UsuarioResponseDTO;
import com.revisacaminhoes.site.requestdto.UsuarioRequestDTO;
import com.revisacaminhoes.site.requestdto.UsuarioUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JavaMailSender mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    // usado pelo Spring Security
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // usamos o e-mail como principal no SecurityContext
        return User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRole().name())
                .disabled(Boolean.FALSE.equals(u.getAtivo())) // se você já adicionou a coluna 'ativo'
                .build();
    }

    // cadastro simples (bootstrap)
    public Usuario criarUsuario(String username, String password, String role) {
        usuarioRepository.findByUsername(username).ifPresent(x -> {
            throw new RuntimeException("Usuário já existe");
        });

        String norm = username.trim().toLowerCase();

        Usuario novo = Usuario.builder()
                .username(norm)
                .email(norm) // <- preenche também o e-mail (compatibilidade)
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role.toUpperCase()))
                .ativo(true)
                .build();

        return usuarioRepository.save(novo);
    }

    // alterar senha do usuário logado
    public void atualizarSenha(AtualizarSenhaRequestDTO dto) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName(); // agora é o e-mail

        Usuario usuario = usuarioRepository.findByEmail(principal)
                .orElseGet(() -> usuarioRepository.findByUsername(principal)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado")));

        if (dto.getSenhaAtual() != null && !dto.getSenhaAtual().isBlank()) {
            if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
                throw new RuntimeException("Senha atual incorreta");
            }
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);
    }

    public Usuario criarUsuarioPorEmail(String email, String password, String role) {
        String norm = email.trim().toLowerCase();
        if (usuarioRepository.existsByEmail(norm)) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        Usuario novo = Usuario.builder()
                .username(norm) // mantém username = e-mail (legado)
                .email(norm)
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role.toUpperCase()))
                .ativo(true)
                .build();

        return usuarioRepository.save(novo);
    }


    // ====== HELPERS ======
    private UsuarioResponseDTO toDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .email(u.getEmail())
                .username(u.getUsername())
                .role(u.getRole().name())
                .ativo(u.getAtivo())
                .build();
    }

    // ====== ADMIN: paginação + CRUD ======
    public Page<UsuarioResponseDTO> page(String q, String role, Boolean ativo, Pageable pageable) {
        var roleEnum = (role == null || role.isBlank()) ? null : Role.valueOf(role.toUpperCase());
        return usuarioRepository.search(
                (q == null || q.isBlank()) ? null : q.trim(),
                ativo,
                roleEnum,
                pageable
        ).map(this::toDTO);
    }

    public UsuarioResponseDTO buscar(Long id) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return toDTO(u);
    }

    public UsuarioResponseDTO criarAdmin(UsuarioRequestDTO dto) {
        String norm = dto.getEmail().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(norm)) {
            throw new RuntimeException("E-mail já cadastrado");
        }
        var u = Usuario.builder()
                .username(norm) // mantém compatibilidade
                .email(norm)
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.valueOf(dto.getRole().toUpperCase()))
                .ativo(true)
                .build();
        u = usuarioRepository.save(u);
        return toDTO(u);
    }

    public UsuarioResponseDTO atualizarAdmin(Long id, UsuarioUpdateRequestDTO dto) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String norm = dto.getEmail().trim().toLowerCase();
            if (usuarioRepository.existsByEmailAndIdNot(norm, id)) {
                throw new RuntimeException("E-mail já cadastrado");
            }
            u.setEmail(norm);
            u.setUsername(norm); // mantém compatibilidade
        }

        if (dto.getRole() != null && !dto.getRole().isBlank()) {
            u.setRole(Role.valueOf(dto.getRole().toUpperCase()));
        }

        if (dto.getAtivo() != null) {
            u.setAtivo(dto.getAtivo());
        }

        if (dto.getNovaSenha() != null && !dto.getNovaSenha().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getNovaSenha()));
        }

        u = usuarioRepository.save(u);
        return toDTO(u);
    }

    public void ativar(Long id) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        u.setAtivo(true);
        usuarioRepository.save(u);
    }

    public void inativar(Long id) {
        var u = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        u.setAtivo(false);
        usuarioRepository.save(u);
    }

    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public void iniciarResetSenha(String email) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            String token = gerarToken();
            usuario.setResetToken(token);
            usuario.setResetTokenExpiresAt(LocalDateTime.now().plusHours(2));
            usuarioRepository.save(usuario);
            enviarEmailReset(usuario.getEmail(), token);
        });
        // Sempre “ok” para não vazar se o e-mail existe ou não
    }

    public void concluirResetSenha(String token, String novaSenha) {
        Usuario u = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        if (u.getResetTokenExpiresAt() == null || u.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }
        u.setPassword(passwordEncoder.encode(novaSenha));
        u.setResetToken(null);
        u.setResetTokenExpiresAt(null);
        usuarioRepository.save(u);
    }

    private String gerarToken() {
        byte[] bytes = new byte[24];
        new SecureRandom().nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private void enviarEmailReset(String email, String token) {
        String link = frontendUrl + "/reset-senha?token=" + token;
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Redefinição de senha");
        msg.setText("Olá! Para redefinir sua senha, acesse: " + link + "\n\nEste link expira em 2 horas.");
        mailSender.send(msg);
    }
}
