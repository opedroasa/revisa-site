package com.revisacaminhoes.site.requestdto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para cadastrar compatibilidade do produto.
 */
@Getter
@Setter
public class CompatibilidadeProdutoRequestDTO {
    private Long modeloId;
    private Integer anoInicial;
    private Integer anoFinal;
}
