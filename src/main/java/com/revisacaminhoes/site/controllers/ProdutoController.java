package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.ProdutoRequestDTO;
import com.revisacaminhoes.site.responsedto.ProdutoResponseDTO;
import com.revisacaminhoes.site.services.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // GET listar todos os produtos
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    // GET listar todos os produtos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    // GET buscar produto específico
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    // POST criar produto
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.criarProduto(dto));
    }

    // PUT atualizar produto
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, dto));
    }

    // PUT inativar produto
    @PutMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        produtoService.inativarProduto(id);
        return ResponseEntity.noContent().build();
    }

    // PUT ativar produto
    @PutMapping("/ativar/{id}")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        produtoService.ativarProduto(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE excluir produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorFiltro(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long modeloId,
            @RequestParam(required = false) Integer ano
    ) {
        return ResponseEntity.ok(produtoService.buscarPorFiltro(marcaId, modeloId, ano));
    }

}
