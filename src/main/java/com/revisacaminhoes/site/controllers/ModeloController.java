package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.ModeloRequestDTO;
import com.revisacaminhoes.site.responsedto.ModeloResponseDTO;
import com.revisacaminhoes.site.services.ModeloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar modelos de caminhão.
 * Usa DTOs para entrada e saída.
 */
@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    // Listar todos os modelos
    @GetMapping
    public ResponseEntity<List<ModeloResponseDTO>> listarTodos() {
        return ResponseEntity.ok(modeloService.listarModelos());
    }

    // Listar modelos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<ModeloResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(modeloService.listarModelosAtivos());
    }

    // Listar modelos de uma marca
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<ModeloResponseDTO>> listarPorMarca(@PathVariable Long marcaId) {
        return ResponseEntity.ok(modeloService.listarPorMarca(marcaId));
    }

    // Buscar modelo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ModeloResponseDTO> buscar(@PathVariable Long id) {
        return modeloService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar modelo
    @PostMapping
    public ResponseEntity<ModeloResponseDTO> criar(@RequestBody ModeloRequestDTO dto) {
        return ResponseEntity.ok(modeloService.criarModelo(dto));
    }

    // Atualizar modelo
    @PutMapping("/{id}")
    public ResponseEntity<ModeloResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ModeloRequestDTO dto
    ) {
        return ResponseEntity.ok(modeloService.atualizarModelo(id, dto));
    }

    // Inativar modelo
    @PutMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        modeloService.inativarModelo(id);
        return ResponseEntity.noContent().build();
    }

    // Ativar modelo
    @PutMapping("/ativar/{id}")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        modeloService.ativarModelo(id);
        return ResponseEntity.noContent().build();
    }

    // Excluir modelo
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        modeloService.excluirModelo(id);
        return ResponseEntity.noContent().build();
    }
}
