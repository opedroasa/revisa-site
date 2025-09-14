package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // login

    @Column(nullable = false)
    private String password; // senha (criptografada)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // papel: ADMIN ou USER
}
