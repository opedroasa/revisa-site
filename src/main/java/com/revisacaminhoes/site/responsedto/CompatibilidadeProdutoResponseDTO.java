package com.revisacaminhoes.site.responsedto;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO de resposta da compatibilidade do produto.
 */
@Getter
@Builder
public class CompatibilidadeProdutoResponseDTO {
    private Long id;
    private String marcaNome;
    private String modeloNome;
    private Integer anoInicial;
    private Integer anoFinal;
}
