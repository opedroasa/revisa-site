package com.revisacaminhoes.site.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ModeloResponseDTO {
    private Long id;
    private String nome;
    private Boolean ativo;
    private Long marcaId;
    private String marcaNome;

}
