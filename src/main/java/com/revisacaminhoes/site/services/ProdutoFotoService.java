package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.*;
import com.revisacaminhoes.site.repositories.*;
import com.revisacaminhoes.site.requestdto.*;
import com.revisacaminhoes.site.responsedto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoFotoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoFotoRepository fotoRepository;
    private final UploadService uploadService;

    public ProdutoFotoService(ProdutoRepository produtoRepository, ProdutoFotoRepository fotoRepository, UploadService uploadService) {
        this.produtoRepository = produtoRepository;
        this.fotoRepository = fotoRepository;
        this.uploadService = uploadService;

    }

    // Adicionar foto ao produto
    public ProdutoFotoResponseDTO adicionarFoto(Long produtoId, ProdutoFotoRequestDTO dto) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ProdutoFoto foto = ProdutoFoto.builder()
                .url(dto.getUrl())
                .publicId(dto.getPublicId())
                .produto(produto)
                .build();

        ProdutoFoto salvo = fotoRepository.save(foto);
        return toResponse(salvo);
    }

    // Listar fotos de um produto
    public List<ProdutoFotoResponseDTO> listarPorProduto(Long produtoId) {
        return fotoRepository.findByProdutoId(produtoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Remover foto específica
    public void excluirFoto(Long fotoId) {
        ProdutoFoto foto = fotoRepository.findById(fotoId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));

        if (foto.getPublicId() != null) {
            uploadService.deletarImagem(foto.getPublicId());
        }

        fotoRepository.delete(foto);
    }

    private ProdutoFotoResponseDTO toResponse(ProdutoFoto foto) {
        return ProdutoFotoResponseDTO.builder()
                .id(foto.getId())
                .url(foto.getUrl())
                .publicId(foto.getPublicId())
                .build();
    }
}
