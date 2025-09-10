package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidade Modelo de caminhão.
 * Cada modelo pertence a uma Marca.
 * Administrador pode cadastrar, atualizar, inativar e excluir modelos.
 */
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
    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;
}
