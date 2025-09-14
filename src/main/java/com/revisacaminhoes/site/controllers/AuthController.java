package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.entities.Usuario;
import com.revisacaminhoes.site.services.UsuarioService;
import com.revisacaminhoes.site.requestdto.AtualizarSenhaRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    public ResponseEntity<String> me(@RequestParam String username) {
        return ResponseEntity.ok("Logado como: " + username);
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Map<String, String>> alterarSenha(@RequestBody AtualizarSenhaRequestDTO dto) {
        usuarioService.atualizarSenha(dto);
        return ResponseEntity.ok(Map.of(
                "mensagem", "Senha alterada com sucesso. Faça login novamente com a nova senha."
        ));
    }
}
