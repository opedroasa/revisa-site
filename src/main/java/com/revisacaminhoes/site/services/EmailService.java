package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.requestdto.CompramosSeuBatidoRequestDTO;
import com.revisacaminhoes.site.requestdto.FaleConoscoRequestDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${revisa.mail.to}")
    private String mailTo;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCompramosSeuBatido(CompramosSeuBatidoRequestDTO dto, List<MultipartFile> fotos) throws Exception {
        final String assunto = "Proposta - Compramos seu batido (" +
                safe(dto.getMarca()) + " " + safe(dto.getModelo()) + " - " + safe(dto.getAnoModelo()) + ")";

        final String html = """
        <div style="font-family:Arial,sans-serif">
          <h2>Compramos seu batido - Solicitação</h2>
          <p><b>Nome:</b> %s</p>
          <p><b>CPF:</b> %s</p>
          <p><b>Telefone:</b> %s</p>
          <p><b>E-mail:</b> %s</p>
          <p><b>Marca:</b> %s</p>
          <p><b>Modelo:</b> %s</p>
          <p><b>Ano-Modelo:</b> %s</p>
          <hr/>
          <p>Fotos em anexo.</p>
        </div>
    """.formatted(
                esc(dto.getNome()),
                esc(dto.getCpf()),
                esc(dto.getTelefone()),
                esc(dto.getEmail() == null ? "" : dto.getEmail()),
                esc(dto.getMarca()),
                esc(dto.getModelo()),
                esc(dto.getAnoModelo())
        );

        // usa Reply-To se o usuário informou e-mail
        String replyTo = (dto.getEmail() != null && !dto.getEmail().isBlank()) ? dto.getEmail() : null;

        enviarComAnexos(assunto, html, replyTo, fotos);
    }

    public void enviarFaleConosco(FaleConoscoRequestDTO dto) throws Exception {
        final String assunto = "Fale Conosco - " + safe(dto.getMotivo()) + " - " + safe(dto.getNome());
        final String html = """
            <div style="font-family:Arial,sans-serif">
              <h2>Fale Conosco</h2>
              <p><b>Nome:</b> %s</p>
              <p><b>E-mail:</b> %s</p>
              <p><b>Motivo:</b> %s</p>
              <p><b>Mensagem:</b></p>
              <p style="white-space:pre-wrap">%s</p>
            </div>
        """.formatted(
                esc(dto.getNome()),
                esc(dto.getEmail()),
                esc(String.valueOf(dto.getMotivo())),
                esc(dto.getMensagem())
        );

        // Reply-To para facilitar responder direto ao usuário
        enviarComAnexos(assunto, html, dto.getEmail(), null);
    }

    private void enviarComAnexos(String assunto, String html, String replyTo, List<MultipartFile> anexos) throws Exception {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, StandardCharsets.UTF_8.name());

        helper.setFrom(mailFrom);
        helper.setTo(mailTo);
        helper.setSubject(assunto);
        helper.setText(html, true);

        if (replyTo != null && !replyTo.isBlank()) {
            helper.setReplyTo(replyTo);
        }

        if (anexos != null) {
            for (MultipartFile file : anexos) {
                if (file != null && !file.isEmpty()) {
                    String filename = sanitize(file.getOriginalFilename());
                    InputStreamSource src = file::getInputStream;
                    helper.addAttachment(filename != null ? filename : "anexo.jpg", src);
                }
            }
        }

        mailSender.send(mime);
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private String safe(Object o) {
        return o == null ? "" : o.toString();
    }

    private String sanitize(String name) {
        if (name == null) return null;
        return name.replaceAll("[\\\\/\\r\\n\\t]", "_");
    }
}
