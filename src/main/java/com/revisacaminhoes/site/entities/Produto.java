package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
        private Double preco;
        private Integer estoque;

        @Column(nullable = false)
        private Boolean ativo = true;

        private LocalDateTime criadoEm = LocalDateTime.now();
        private LocalDateTime atualizadoEm = LocalDateTime.now();
    }
