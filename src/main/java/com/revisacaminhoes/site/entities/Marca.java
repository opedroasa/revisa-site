package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entidade Marca do caminhão.
 * Administrador pode criar, atualizar, inativar marcas.
 */
@Entity
@Table(name = "marca")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da marca (ex: Volvo, Fiat)
    @Column(nullable = false, unique = true)
    private String nome;

    // Marca ativa ou inativa
    @Column(nullable = false)
    private Boolean ativo = true;

    // Data de criação (para controle)
    private LocalDateTime criadoEm = LocalDateTime.now();

    // Data de atualização
    private LocalDateTime atualizadoEm = LocalDateTime.now();
}
