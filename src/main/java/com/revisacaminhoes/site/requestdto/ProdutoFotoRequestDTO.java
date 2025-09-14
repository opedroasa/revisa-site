package com.revisacaminhoes.site.requestdto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoFotoRequestDTO {
    private String url;
    private String publicId;
}
