package com.revisacaminhoes.site.responsedto;

public class EmailResponseDTO {

    private String mensagem;

    public EmailResponseDTO() {}
    public EmailResponseDTO(String mensagem) { this.mensagem = mensagem; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
