package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.entities.Marca;
import com.revisacaminhoes.site.services.MarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar marcas de caminhão.
 * Todas as operações pensadas para serem feitas pelo administrador.
 */
@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService marcaService;

    public MarcaController(MarcaService marcaService) {
        this.marcaService = marcaService;
    }

    // Listar todas marcas ativas
    @GetMapping
    public ResponseEntity<List<Marca>> listar() {
        return ResponseEntity.ok(marcaService.listarMarcasAtivas());
    }

    // Buscar marca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Marca> buscar(@PathVariable Long id) {
        return marcaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar nova marca
    @PostMapping
    public ResponseEntity<Marca> criar(@RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.criarMarca(marca));
    }

    // Atualizar nome da marca
    @PutMapping("/{id}")
    public ResponseEntity<Marca> atualizar(@PathVariable Long id, @RequestBody Marca marca) {
        return ResponseEntity.ok(marcaService.atualizarMarca(id, marca.getNome()));
    }

    // Inativar marca
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        marcaService.inativarMarca(id);
        return ResponseEntity.noContent().build();
    }
}
