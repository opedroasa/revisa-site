package com.revisacaminhoes.site.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailRequestDTO {
    @NotBlank @Email
    private String email;
}
