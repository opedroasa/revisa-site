package com.revisacaminhoes.site.requestdto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoFotoRequestDTO {
    private String url; // URL da imagem enviada pelo admin
}
