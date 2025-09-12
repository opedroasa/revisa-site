package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "compatibilidade_produto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompatibilidadeProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Produto associado
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Modelo associado
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    @Column(nullable = false)
    private Integer anoInicial;

    @Column(nullable = false)
    private Integer anoFinal;
}
