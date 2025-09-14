package com.revisacaminhoes.site.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarSenhaRequestDTO {
    private String senhaAtual;
    private String novaSenha;
}
