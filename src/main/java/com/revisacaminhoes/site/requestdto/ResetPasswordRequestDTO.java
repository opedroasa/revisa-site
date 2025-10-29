package com.revisacaminhoes.site.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResetPasswordRequestDTO {
    @NotBlank
    private String token;

    @NotBlank
    private String novaSenha;
}
