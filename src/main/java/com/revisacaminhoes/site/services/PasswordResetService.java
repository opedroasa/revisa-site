package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.PasswordResetToken;
import com.revisacaminhoes.site.entities.Usuario;
import com.revisacaminhoes.site.repositories.PasswordResetTokenRepository;
import com.revisacaminhoes.site.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    // URL base da página do front que recebe o token (ex.: http://localhost:3000/recuperar-senha)
    @Value("${app.reset.base-url:http://localhost:3000/ResetPasswordPage}")
    private String resetBaseUrl;

    public PasswordResetService(
            UsuarioRepository usuarioRepository,
            PasswordResetTokenRepository tokenRepository,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void solicitarReset(String email) {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado."));

        // gera token válido por 30 minutos
        String token = UUID.randomUUID().toString().replace("-", "");
        PasswordResetToken prt = PasswordResetToken.builder()
                .token(token)
                .usuario(user)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        tokenRepository.save(prt);

        String link = resetBaseUrl + "?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Redefinição de senha - Revisa Caminhões");
        msg.setText("""
                Olá,

                Recebemos um pedido para redefinir sua senha.
                Clique no link abaixo para criar uma nova senha (válido por 30 minutos):

                %s

                Se você não solicitou, ignore este e-mail.
                """.formatted(link));

        mailSender.send(msg);
    }

    public void validarToken(String token) {
        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido."));
        if (prt.isExpired()) throw new RuntimeException("Token expirado.");
        if (prt.isUsed()) throw new RuntimeException("Token já utilizado.");
    }

    public void resetarSenha(String token, String novaSenha) {
        PasswordResetToken prt = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido."));
        if (prt.isExpired()) throw new RuntimeException("Token expirado.");
        if (prt.isUsed()) throw new RuntimeException("Token já utilizado.");

        Usuario u = prt.getUsuario();
        u.setPassword(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(u);

        prt.setUsedAt(LocalDateTime.now());
        tokenRepository.save(prt);
    }
}
