package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "modelo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do modelo (ex: FH, Stralis)
    @Column(nullable = false, unique = true)
    private String nome;

    // Modelo ativo ou inativo
    @Column(nullable = false)
    private Boolean ativo = true;

    // Data de criação
    private LocalDateTime criadoEm = LocalDateTime.now();

    // Data de atualização
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    // Relação com a Marca
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    // Relacionamento com compatibilidades
    @OneToMany(
            mappedBy = "modelo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CompatibilidadeProduto> compatibilidades = new ArrayList<>();
}