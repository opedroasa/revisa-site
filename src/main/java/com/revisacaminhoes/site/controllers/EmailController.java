package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.CompramosSeuBatidoRequestDTO;
import com.revisacaminhoes.site.requestdto.FaleConoscoRequestDTO;
import com.revisacaminhoes.site.responsedto.EmailResponseDTO;
import com.revisacaminhoes.site.services.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/email")
@Validated
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Compramos seu batido:
     * Enviar como multipart/form-data com:
     * - dados: JSON (application/json) do CompramosSeuBatidoRequestDTO
     * - fotos: múltiplos arquivos (file[]), opcional
     */
    @PostMapping(
            path = "/compramos-seu-batido",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<EmailResponseDTO> compramosSeuBatido(
            @RequestPart("dados") @Valid CompramosSeuBatidoRequestDTO dados,
            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos
    ) throws Exception {
        emailService.enviarCompramosSeuBatido(dados, fotos);
        return ResponseEntity.ok(new EmailResponseDTO("Solicitação enviada com sucesso."));
    }

    /**
     * Fale Conosco:
     * Enviar como application/json
     */
    @PostMapping(
            path = "/fale-conosco",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EmailResponseDTO> faleConosco(
            @RequestBody @Valid FaleConoscoRequestDTO dados
    ) throws Exception {
        emailService.enviarFaleConosco(dados);
        return ResponseEntity.ok(new EmailResponseDTO("Mensagem enviada com sucesso."));
    }
}
