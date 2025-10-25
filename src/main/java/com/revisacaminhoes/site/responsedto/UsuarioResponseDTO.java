package com.revisacaminhoes.site.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @Builder
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private String username;
    private String role;   // "ADMIN" | "USER"
    private Boolean ativo;
}
