package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.responsedto.SiteSettingsDTO;
import com.revisacaminhoes.site.services.SiteSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site")
public class SiteSettingsController {

    private final SiteSettingsService service;
    public SiteSettingsController(SiteSettingsService service) { this.service = service; }

    // público (front lê aqui)
    @GetMapping("/settings/public")
    public ResponseEntity<SiteSettingsDTO> getPublic() {
        return ResponseEntity.ok(service.getPublic());
    }

    // admin (protegido por BasicAuth igual às outras rotas mutáveis)
    @GetMapping("/settings")
    public ResponseEntity<SiteSettingsDTO> getAdmin() {
        return ResponseEntity.ok(service.getAdmin());
    }

    @PutMapping("/settings")
    public ResponseEntity<SiteSettingsDTO> update(@RequestBody SiteSettingsDTO dto) {
        // aqui você pode validar telefone (só dígitos, tamanho, etc.) se quiser
        return ResponseEntity.ok(service.update(dto));
    }
}
