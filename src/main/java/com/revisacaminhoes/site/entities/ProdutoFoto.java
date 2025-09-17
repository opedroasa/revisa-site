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

    // URL pública da imagem
    @Column(nullable = false)
    private String url;

    // Identificador do Cloudinary (necessário para excluir)
    @Column(nullable = false)
    private String publicId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(nullable = false)
    private boolean destaque = false;
}