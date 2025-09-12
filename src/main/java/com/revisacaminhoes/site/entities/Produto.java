package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @Column(nullable = false)
    private Boolean ativo = true;

    private LocalDateTime criadoEm = LocalDateTime.now();
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    // Relacionamento com compatibilidades
    @OneToMany(
            mappedBy = "produto",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default // garante que mesmo usando o builder, a lista não fica null
    private List<CompatibilidadeProduto> compatibilidades = new ArrayList<>();
}
