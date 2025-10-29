package com.revisacaminhoes.site.requestdto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioUpdateRequestDTO {
    @Email
    private String email;       // opcional

    private String role;        // opcional: "ADMIN" | "USER"

    private Boolean ativo;      // opcional

    private String novaSenha;   // opcional: altera senha se informado
}
