package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.requestdto.AjusteEstoqueRequestDTO;
import com.revisacaminhoes.site.responsedto.ProdutoResponseDTO;
import com.revisacaminhoes.site.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoEstoqueController {

    private final ProdutoService produtoService;

    public ProdutoEstoqueController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Define estoque absoluto (pode ser zero).
     * PUT /api/produtos/{id}/estoque/definir
     */
    @PutMapping("/{id}/estoque/definir")
    public ResponseEntity<ProdutoResponseDTO> definir(
            @PathVariable Long id,
            @Valid @RequestBody AjusteEstoqueRequestDTO dto
    ) {
        return ResponseEntity.ok(produtoService.definirEstoque(id, dto.getQuantidade()));
    }

    /**
     * Entrada de estoque (soma).
     * PUT /api/produtos/{id}/estoque/entrada
     */
    @PutMapping("/{id}/estoque/entrada")
    public ResponseEntity<ProdutoResponseDTO> entrada(
            @PathVariable Long id,
            @Valid @RequestBody AjusteEstoqueRequestDTO dto
    ) {
        return ResponseEntity.ok(produtoService.entradaEstoque(id, dto.getQuantidade()));
    }

    /**
     * Saída de estoque (subtrai).
     * PUT /api/produtos/{id}/estoque/saida
     */
    @PutMapping("/{id}/estoque/saida")
    public ResponseEntity<ProdutoResponseDTO> saida(
            @PathVariable Long id,
            @Valid @RequestBody AjusteEstoqueRequestDTO dto
    ) {
        return ResponseEntity.ok(produtoService.saidaEstoque(id, dto.getQuantidade()));
    }
}
