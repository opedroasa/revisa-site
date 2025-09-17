package com.revisacaminhoes.site.controllers;

import com.revisacaminhoes.site.repositories.ProdutoRepository;
import com.revisacaminhoes.site.requestdto.ProdutoFotoRequestDTO;
import com.revisacaminhoes.site.responsedto.ProdutoFotoResponseDTO;
import com.revisacaminhoes.site.services.ProdutoFotoService;
import com.revisacaminhoes.site.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoFotoController {

    private final ProdutoFotoService fotoService;
    private final UploadService uploadService; // injeção do serviço de upload
    private final ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoFotoService service;


    public ProdutoFotoController(ProdutoFotoService fotoService, UploadService uploadService, ProdutoRepository produtoRepository
    ) {
        this.fotoService = fotoService;
        this.uploadService = uploadService;
        this.produtoRepository = produtoRepository;

    }

    // Adicionar foto ao produto (com URL já existente)
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

    @PostMapping("/{produtoId}/fotos/upload")
    public ResponseEntity<ProdutoFotoResponseDTO> uploadFoto(
            @PathVariable Long produtoId,
            @RequestParam("file") MultipartFile file
    ) {
        // Verifica se produto existe ANTES de enviar
        produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Map<String, String> uploadResult = uploadService.uploadImagem(file);

        ProdutoFotoRequestDTO dto = ProdutoFotoRequestDTO.builder()
                .url(uploadResult.get("url"))
                .publicId(uploadResult.get("publicId"))
                .build();

        return ResponseEntity.ok(fotoService.adicionarFoto(produtoId, dto));
    }

    @PutMapping("/{produtoId}/fotos/{fotoId}/destaque")
    public ResponseEntity<Void> definirDestaque(
            @PathVariable Long produtoId,
            @PathVariable Long fotoId) {
        service.definirDestaque(produtoId, fotoId);
        return ResponseEntity.noContent().build(); // 204
    }

}
