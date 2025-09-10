package com.revisacaminhoes.site.requestdto;

import lombok.*;

/**
 * DTO usado para entrada de dados (criação e atualização).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeloRequestDTO {
    private String nome;
    private Long marcaId;
}