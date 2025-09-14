package com.revisacaminhoes.site.responsedto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoFotoResponseDTO {
    private Long id;
    private String url;
    private String publicId;
}
