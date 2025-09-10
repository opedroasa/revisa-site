package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "compatibilidade_produto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompatibilidadeProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Produto associado
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    // Modelo associado
    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    private Integer anoInicial;
    private Integer anoFinal;
}
