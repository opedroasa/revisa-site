package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.entities.Modelo;
import com.revisacaminhoes.site.services.ModeloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar modelos de caminhão.
 */
@RestController
@RequestMapping("/api/modelos")
public class ModeloController {

    private final ModeloService modeloService;

    public ModeloController(ModeloService modeloService) {
        this.modeloService = modeloService;
    }

    // Listar todos os modelos, ativos e inativos
    @GetMapping
    public ResponseEntity<List<Modelo>> listarTodos() {
        return ResponseEntity.ok(modeloService.listarModelos());
    }

    // Listar todos modelos ativos
    @GetMapping ("/ativos")
    public ResponseEntity<List<Modelo>> listarAtivos() {
        return ResponseEntity.ok(modeloService.listarModelosAtivos());
    }

    // Listar modelos de uma marca
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<List<Modelo>> listarPorMarca(@PathVariable Long marcaId) {
        return ResponseEntity.ok(modeloService.listarPorMarca(marcaId));
    }

    // Buscar modelo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Modelo> buscar(@PathVariable Long id) {
        return modeloService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar modelo
    @PostMapping
    public ResponseEntity<Modelo> criar(@RequestBody Modelo modelo) {
        return ResponseEntity.ok(modeloService.criarModelo(modelo));
    }

    // Atualizar modelo
    @PutMapping("/{id}")
    public ResponseEntity<Modelo> atualizar(@PathVariable Long id, @RequestBody Modelo modelo) {
        return ResponseEntity.ok(
                modeloService.atualizarModelo(id, modelo.getNome(), modelo.getAnoFabricacao())
        );
    }

    // Inativar modelo
    @DeleteMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        modeloService.inativarModelo(id);
        return ResponseEntity.noContent().build();
    }

    // Excluir modelo
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        modeloService.excluirModelo(id);
        return ResponseEntity.noContent().build();
    }

}
