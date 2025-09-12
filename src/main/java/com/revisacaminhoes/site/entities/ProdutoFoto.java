package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produto_foto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // URL da imagem (armazenada em nuvem)
    @Column(nullable = false)
    private String url;

    // Produto ao qual a foto pertence
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
}
