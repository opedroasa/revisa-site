package com.revisacaminhoes.site.requestdto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoRequestDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Boolean ativo;

    // Compatibilidades enviadas no cadastro
    private List<CompatibilidadeProdutoRequestDTO> compatibilidades;
}
