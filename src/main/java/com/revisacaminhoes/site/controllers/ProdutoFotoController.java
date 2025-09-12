package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.ProdutoFotoRequestDTO;
import com.revisacaminhoes.site.responsedto.ProdutoFotoResponseDTO;
import com.revisacaminhoes.site.services.ProdutoFotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoFotoController {

    private final ProdutoFotoService fotoService;

    public ProdutoFotoController(ProdutoFotoService fotoService) {
        this.fotoService = fotoService;
    }

    // Adicionar foto ao produto
    @PostMapping("/{produtoId}/fotos")
    public ResponseEntity<ProdutoFotoResponseDTO> adicionarFoto(
            @PathVariable Long produtoId,
            @RequestBody ProdutoFotoRequestDTO dto
    ) {
        return ResponseEntity.ok(fotoService.adicionarFoto(produtoId, dto));
    }

    // Listar fotos do produto
    @GetMapping("/{produtoId}/fotos")
    public ResponseEntity<List<ProdutoFotoResponseDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(fotoService.listarPorProduto(produtoId));
    }

    // Excluir foto
    @DeleteMapping("/fotos/{fotoId}")
    public ResponseEntity<Void> excluirFoto(@PathVariable Long fotoId) {
        fotoService.excluirFoto(fotoId);
        return ResponseEntity.noContent().build();
    }
}
