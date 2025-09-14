package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.CompatibilidadeProduto;
import com.revisacaminhoes.site.entities.Modelo;
import com.revisacaminhoes.site.entities.Produto;
import com.revisacaminhoes.site.repositories.CompatibilidadeProdutoRepository;
import com.revisacaminhoes.site.repositories.ModeloRepository;
import com.revisacaminhoes.site.repositories.ProdutoRepository;
import com.revisacaminhoes.site.requestdto.CompatibilidadeProdutoRequestDTO;
import com.revisacaminhoes.site.requestdto.ProdutoRequestDTO;
import com.revisacaminhoes.site.responsedto.CompatibilidadeProdutoResponseDTO;
import com.revisacaminhoes.site.responsedto.ProdutoFotoResponseDTO;
import com.revisacaminhoes.site.responsedto.ProdutoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CompatibilidadeProdutoRepository compatibilidadeRepository;
    private final ModeloRepository modeloRepository;
    private final UploadService uploadService; // ✅ INJETADO

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CompatibilidadeProdutoRepository compatibilidadeRepository,
            ModeloRepository modeloRepository,
            UploadService uploadService // ✅ INJETADO NO CONSTRUTOR
    ) {
        this.produtoRepository = produtoRepository;
        this.compatibilidadeRepository = compatibilidadeRepository;
        this.modeloRepository = modeloRepository;
        this.uploadService = uploadService;
    }

    // ===== CRUD BÁSICO =====

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .estoque(dto.getEstoque())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        // Valida compatibilidades ANTES de salvar
        if (dto.getCompatibilidades() != null) {
            for (CompatibilidadeProdutoRequestDTO c : dto.getCompatibilidades()) {
                Modelo modelo = modeloRepository.findById(c.getModeloId())
                        .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));

                CompatibilidadeProduto compatibilidade = CompatibilidadeProduto.builder()
                        .produto(produto)
                        .modelo(modelo)
                        .anoInicial(c.getAnoInicial())
                        .anoFinal(c.getAnoFinal())
                        .build();

                produto.getCompatibilidades().add(compatibilidade);
            }
        }

        Produto salvo = produtoRepository.save(produto);
        return toResponse(salvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setAtualizadoEm(LocalDateTime.now());

        // Recria compatibilidades (só salva se todas forem válidas)
        produto.getCompatibilidades().clear();
        if (dto.getCompatibilidades() != null) {
            for (CompatibilidadeProdutoRequestDTO c : dto.getCompatibilidades()) {
                Modelo modelo = modeloRepository.findById(c.getModeloId())
                        .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));

                CompatibilidadeProduto compatibilidade = CompatibilidadeProduto.builder()
                        .produto(produto)
                        .modelo(modelo)
                        .anoInicial(c.getAnoInicial())
                        .anoFinal(c.getAnoFinal())
                        .build();

                produto.getCompatibilidades().add(compatibilidade);
            }
        }

        Produto atualizado = produtoRepository.save(produto);
        return toResponse(atualizado);
    }

    public void ativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(true);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);
    }

    public void inativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(false);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);
    }

    @Transactional
    public void excluirProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Excluir imagens no Cloudinary antes de remover do banco
        produto.getFotos().forEach(f -> {
            // Pode existir dado antigo sem publicId; proteja
            try {
                // se você deixou NOT NULL, esse if é opcional
                var publicIdField = f.getPublicId();
                if (publicIdField != null && !publicIdField.isBlank()) {
                    uploadService.deletarImagem(publicIdField);
                }
            } catch (Exception ignored) {
                // não derrube a exclusão do produto se um delete de imagem falhar
            }
        });

        produtoRepository.delete(produto);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> listarAtivos() {
        return produtoRepository.findAll().stream()
                .filter(Produto::getAtivo)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toResponse(produto);
    }

    public List<ProdutoResponseDTO> buscarPorFiltro(Long marcaId, Long modeloId, Integer ano) {
        List<CompatibilidadeProduto> compatibilidades =
                compatibilidadeRepository.buscarProdutosCompatíveis(marcaId, modeloId, ano);

        return compatibilidades.stream()
                .map(CompatibilidadeProduto::getProduto)
                .distinct()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ===== AJUSTE DE ESTOQUE =====

    public ProdutoResponseDTO definirEstoque(Long id, int quantidade) {
        if (quantidade < 0) throw new RuntimeException("Quantidade não pode ser negativa.");
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setEstoque(quantidade);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);

        return toResponse(produto);
    }

    public ProdutoResponseDTO entradaEstoque(Long id, int quantidade) {
        if (quantidade <= 0) throw new RuntimeException("Quantidade deve ser maior que zero.");
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setEstoque(produto.getEstoque() + quantidade);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);

        return toResponse(produto);
    }

    public ProdutoResponseDTO saidaEstoque(Long id, int quantidade) {
        if (quantidade <= 0) throw new RuntimeException("Quantidade deve ser maior que zero.");
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int novoEstoque = produto.getEstoque() - quantidade;
        if (novoEstoque < 0) throw new RuntimeException("Saída maior que o estoque disponível.");

        produto.setEstoque(novoEstoque);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);

        return toResponse(produto);
    }

    // ===== CONVERSÃO =====

    private ProdutoResponseDTO toResponse(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .estoque(produto.getEstoque())
                .ativo(produto.getAtivo())
                .compatibilidades(
                        produto.getCompatibilidades().stream()
                                .map(c -> CompatibilidadeProdutoResponseDTO.builder()
                                        .id(c.getId())
                                        .modeloNome(c.getModelo().getNome())
                                        .marcaNome(c.getModelo().getMarca().getNome())
                                        .anoInicial(c.getAnoInicial())
                                        .anoFinal(c.getAnoFinal())
                                        .build())
                                .collect(Collectors.toList())
                )
                .fotos(
                        produto.getFotos().stream()
                                .map(f -> ProdutoFotoResponseDTO.builder()
                                        .id(f.getId())
                                        .url(f.getUrl())
                                        .publicId(f.getPublicId())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
