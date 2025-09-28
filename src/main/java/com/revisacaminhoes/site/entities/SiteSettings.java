package com.revisacaminhoes.site.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "site_settings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SiteSettings {

    @Id
    private Long id; // sempre 1

    @Column(name = "whatsapp_phone", length = 32)
    private String whatsappPhone; // ex: 5534996614723 (só dígitos)

    @Column(name = "about_html", columnDefinition = "TEXT")
    private String aboutHtml; // HTML simples

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
