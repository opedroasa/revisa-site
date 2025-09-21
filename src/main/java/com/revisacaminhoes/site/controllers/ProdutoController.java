package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.ProdutoRequestDTO;
import com.revisacaminhoes.site.responsedto.ProdutoListItemDTO;
import com.revisacaminhoes.site.responsedto.ProdutoResponseDTO;
import com.revisacaminhoes.site.services.ProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.criarProduto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(produtoService.atualizarProduto(id, dto));
    }

    @PutMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        produtoService.inativarProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/ativar/{id}")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        produtoService.ativarProduto(id);
        return ResponseEntity.noContent().build();
    }

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

    // /api/produtos/page?page=0&size=20&sort=nome,asc
    @GetMapping("/page")
    public ResponseEntity<Page<ProdutoListItemDTO>> listarPage(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "true") Boolean somenteAtivos
    ) {
        Page<ProdutoListItemDTO> page = produtoService.listarPaginado(Boolean.TRUE.equals(somenteAtivos), pageable);
        return ResponseEntity.ok(page);
    }

    // /api/produtos/page/filtro?marcaId=1&modeloId=1&ano=2015&q=correia&page=0&size=20&sort=nome,asc
    @GetMapping("/page/filtro")
    public ResponseEntity<Page<ProdutoListItemDTO>> listarPageFiltrado(
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long modeloId,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String q,
            @RequestParam(required = false, defaultValue = "true") Boolean somenteAtivos,
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ProdutoListItemDTO> page = produtoService.listarPaginadoFiltrado(
                marcaId, modeloId, ano,
                Boolean.TRUE.equals(somenteAtivos),
                pageable,
                q
        );
        return ResponseEntity.ok(page);
    }
}
