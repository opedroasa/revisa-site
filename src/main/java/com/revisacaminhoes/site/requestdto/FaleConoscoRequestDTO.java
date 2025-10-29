package com.revisacaminhoes.site.requestdto;

import com.revisacaminhoes.site.entities.MotivoContato;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FaleConoscoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotNull(message = "Motivo é obrigatório")
    private MotivoContato motivo;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(max = 1500, message = "Mensagem muito longa (máx. 1500 caracteres)")
    private String mensagem;

    // getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public MotivoContato getMotivo() { return motivo; }
    public void setMotivo(MotivoContato motivo) { this.motivo = motivo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
