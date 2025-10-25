package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.entities.Usuario;
import com.revisacaminhoes.site.services.UsuarioService;
import com.revisacaminhoes.site.requestdto.AtualizarSenhaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import com.revisacaminhoes.site.requestdto.EmailRequestDTO;
import com.revisacaminhoes.site.requestdto.ResetPasswordRequestDTO;
import com.revisacaminhoes.site.services.PasswordResetService;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordResetService passwordResetService;

    public AuthController(UsuarioService usuarioService,
                          PasswordResetService passwordResetService) {
        this.usuarioService = usuarioService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(defaultValue = "ADMIN") String role
    ) {
        return ResponseEntity.ok(usuarioService.criarUsuario(username, password, role));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String,Object>> me(Authentication auth) {
        return ResponseEntity.ok(Map.of(
                "username", auth.getName()
        ));
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Map<String, String>> alterarSenha(@RequestBody AtualizarSenhaRequestDTO dto) {
        usuarioService.atualizarSenha(dto);
        return ResponseEntity.ok(Map.of(
                "mensagem", "Senha alterada com sucesso. Faça login novamente com a nova senha."
        ));
    }

    @PostMapping("/register-email")
    public ResponseEntity<Usuario> registerEmail(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "ADMIN") String role
    ) {
        return ResponseEntity.ok(usuarioService.criarUsuarioPorEmail(email, password, role));
    }

    // --- Recuperação de senha ---

    @PostMapping("/password/forgot")
    public ResponseEntity<Map<String,String>> forgot(@Valid @RequestBody EmailRequestDTO dto) {
        passwordResetService.solicitarReset(dto.getEmail());
        return ResponseEntity.ok(Map.of("mensagem", "Se o e-mail existir, enviamos instruções para redefinir a senha."));
    }

    @GetMapping("/password/validate")
    public ResponseEntity<Map<String,String>> validate(@RequestParam String token) {
        passwordResetService.validarToken(token);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Map<String,String>> reset(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        passwordResetService.resetarSenha(dto.getToken(), dto.getNovaSenha());
        return ResponseEntity.ok(Map.of("mensagem", "Senha redefinida com sucesso."));
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, String>> check(Authentication auth) {
        return ResponseEntity.ok(Map.of("user", auth.getName()));
    }
}
