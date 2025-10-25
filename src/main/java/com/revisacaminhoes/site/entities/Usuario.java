package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // legado (não será mais usado para login)

    @Column(nullable = false, unique = true)
    private String email;    // NOVO: login oficial

    @Column(nullable = false)
    private String password; // hash BCrypt

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;       // ADMIN ou USER

    @Column(nullable = false)
    private Boolean ativo = true; // NOVO

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expires_at")
    private LocalDateTime resetTokenExpiresAt;
}
