package com.revisacaminhoes.site.responsedto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CompatibilidadeProdutoResponseDTO {
    private Long id;

    // NOVOS: para o front conseguir pré-selecionar os selects
    private Long marcaId;
    private Long modeloId;

    // Já existiam
    private String marcaNome;
    private String modeloNome;

    private Integer anoInicial;
    private Integer anoFinal;
}
