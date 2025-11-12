package com.revisacaminhoes.site.requestdto;

import com.revisacaminhoes.site.validation.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

// (se quiser validar CPF/telefone depois, a gente adiciona @Pattern)

public class CompramosSeuBatidoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido") // <<< APLICAR ANNOTATION
    private String cpf;;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Ano-Modelo é obrigatório")
    private String anoModelo;

    @Email(message = "E-mail inválido")
    private String email; // opcional


    // getters e setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getAnoModelo() { return anoModelo; }
    public void setAnoModelo(String anoModelo) { this.anoModelo = anoModelo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}