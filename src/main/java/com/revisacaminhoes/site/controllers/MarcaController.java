package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.MarcaRequestDTO;
import com.revisacaminhoes.site.responsedto.MarcaResponseDTO;
import com.revisacaminhoes.site.services.MarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    // Listar todas marcas
    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> listar() {
        return ResponseEntity.ok(marcaService.listarMarcas());
    }

    // Listar todas marcas ativas
    @GetMapping("/ativas")
    public ResponseEntity<List<MarcaResponseDTO>> listarAtivas() {
        return ResponseEntity.ok(marcaService.listarMarcasAtivas());
    }

    // Buscar marca por ID
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> buscar(@PathVariable Long id) {
        return marcaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar nova marca
    @PostMapping
    public ResponseEntity<MarcaResponseDTO> criar(@RequestBody MarcaRequestDTO dto) {
        return ResponseEntity.ok(marcaService.criarMarca(dto));
    }

    // Atualizar nome da marca
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> atualizar(@PathVariable Long id, @RequestBody MarcaRequestDTO dto) {
        return ResponseEntity.ok(marcaService.atualizarMarca(id, dto));
    }

    // Inativar marca
    @PutMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        marcaService.inativarMarca(id);
        return ResponseEntity.noContent().build();
    }

    // Ativar marca
    @PutMapping("/ativar/{id}")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        marcaService.ativarMarca(id);
        return ResponseEntity.noContent().build();
    }

    // Excluir marca
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        marcaService.excluirMarca(id);
        return ResponseEntity.noContent().build();
    }
}
