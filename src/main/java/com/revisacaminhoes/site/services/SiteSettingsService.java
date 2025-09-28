package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.SiteSettings;
import com.revisacaminhoes.site.repositories.SiteSettingsRepository;
import com.revisacaminhoes.site.responsedto.SiteSettingsDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SiteSettingsService {
    private final SiteSettingsRepository repo;

    public SiteSettingsService(SiteSettingsRepository repo) { this.repo = repo; }

    private SiteSettings getOrCreate() {
        return repo.findById(1L).orElseGet(() -> {
            SiteSettings s = SiteSettings.builder()
                    .id(1L)
                    .whatsappPhone(null)
                    .aboutHtml("<h1>Sobre nós</h1><p>Lorem ipsum...</p>")
                    .updatedAt(LocalDateTime.now())
                    .build();
            return repo.save(s);
        });
    }

    public SiteSettingsDTO getPublic() {
        SiteSettings s = getOrCreate();
        return SiteSettingsDTO.builder()
                .whatsappPhone(s.getWhatsappPhone())
                .aboutHtml(s.getAboutHtml())
                .build();
    }

    public SiteSettingsDTO getAdmin() {
        return getPublic();
    }

    public SiteSettingsDTO update(SiteSettingsDTO dto) {
        SiteSettings s = getOrCreate();
        s.setWhatsappPhone(dto.getWhatsappPhone());
        s.setAboutHtml(dto.getAboutHtml());
        s.setUpdatedAt(LocalDateTime.now());
        repo.save(s);
        return getPublic();
    }
}
