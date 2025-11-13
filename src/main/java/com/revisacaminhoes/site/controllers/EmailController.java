package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.CompramosSeuBatidoRequestDTO;
import com.revisacaminhoes.site.requestdto.FaleConoscoRequestDTO;
import com.revisacaminhoes.site.responsedto.EmailResponseDTO;
import com.revisacaminhoes.site.services.EmailService;
import jakarta.servlet.http.HttpServletRequest; // <<< IMPORTADO
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
            @RequestPart(value = "fotos", required = false) List<MultipartFile> fotos,
            HttpServletRequest request // <<< INJETADO REQUEST
    ) throws Exception {

        // <<< CAPTURADO O IP >>>
        String clientIp = getClientIp(request);

        // <<< IP PASSADO PARA O SERVICE >>>
        emailService.enviarCompramosSeuBatido(dados, fotos, clientIp);

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
            @RequestBody @Valid FaleConoscoRequestDTO dados,
            HttpServletRequest request // <<< INJETADO REQUEST
    ) throws Exception {

        // <<< CAPTURADO O IP >>>
        String clientIp = getClientIp(request);

        // <<< IP PASSADO PARA O SERVICE >>>
        emailService.enviarFaleConosco(dados, clientIp);

        return ResponseEntity.ok(new EmailResponseDTO("Mensagem enviada com sucesso."));
    }

    /**
     * Helper para pegar o IP do cliente, considerando proxies (X-Forwarded-For).
     */
    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            // Tenta pegar do header de proxy (ex: NGINX, Cloudflare)
            remoteAddr = request.getHeader("X-Forwarded-For");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                // Se não tiver, pega o IP da conexão direta
                remoteAddr = request.getRemoteAddr();
            } else if (remoteAddr.contains(",")) {
                // Se houver múltiplos IPs (ex: proxy1, proxy2, client), pega o primeiro
                remoteAddr = remoteAddr.split(",")[0].trim();
            }
        }
        return remoteAddr;
    }
}