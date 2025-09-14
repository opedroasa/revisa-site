package com.revisacaminhoes.site.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AjusteEstoqueRequestDTO {

    /**
     * Quantidade para ajuste.
     * - definir: valor absoluto (pode ser zero)
     * - entrada: somado ao estoque atual (deve ser > 0)
     * - saida: subtraído do estoque atual (deve ser > 0 e não pode deixar negativo)
     */
    @NotNull
    @Min(0)
    private Integer quantidade;

    /**
     * Campo livre, opcional, caso queira registrar o motivo do ajuste.
     * (Apenas armazenado no front, não usamos no back por enquanto)
     */
    private String observacao;
}
