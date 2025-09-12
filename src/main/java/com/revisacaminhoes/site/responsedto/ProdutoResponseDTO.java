package com.revisacaminhoes.site.responsedto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Boolean ativo;
    private List<CompatibilidadeProdutoResponseDTO> compatibilidades;
}
