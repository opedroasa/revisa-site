package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.*;
import com.revisacaminhoes.site.repositories.*;
import com.revisacaminhoes.site.requestdto.*;
import com.revisacaminhoes.site.responsedto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CompatibilidadeProdutoRepository compatibilidadeRepository;
    private final ModeloRepository modeloRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            CompatibilidadeProdutoRepository compatibilidadeRepository,
            ModeloRepository modeloRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.compatibilidadeRepository = compatibilidadeRepository;
        this.modeloRepository = modeloRepository;
    }

    // Criar produto com compatibilidades
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

        // Compatibilidades (apenas modeloId, anoInicial, anoFinal)
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

    // Atualizar produto
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setAtualizadoEm(LocalDateTime.now());

        // Limpa compatibilidades antigas
        produto.getCompatibilidades().clear();

        // Recria compatibilidades
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

    // Ativar produto
    public void ativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(true);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);
    }

    // Inativar produto
    public void inativarProduto(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(false);
        produto.setAtualizadoEm(LocalDateTime.now());
        produtoRepository.save(produto);
    }

    // Excluir produto
    public void excluirProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    // Listar todos os produtos
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Listar produtos ativos
    public List<ProdutoResponseDTO> listarAtivos() {
        return produtoRepository.findAll().stream()
                .filter(Produto::getAtivo)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Buscar produto por ID
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toResponse(produto);
    }

    // Buscar por filtro (marcaId, modeloId, ano)
    public List<ProdutoResponseDTO> buscarPorFiltro(Long marcaId, Long modeloId, Integer ano) {
        List<CompatibilidadeProduto> compatibilidades = compatibilidadeRepository.buscarProdutosCompatíveis(marcaId, modeloId, ano);

        return compatibilidades.stream()
                .map(CompatibilidadeProduto::getProduto)
                .distinct()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Conversão para ResponseDTO
    private ProdutoResponseDTO toResponse(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .estoque(produto.getEstoque())
                .ativo(produto.getAtivo())
                .compatibilidades(produto.getCompatibilidades() != null ?
                        produto.getCompatibilidades().stream().map(c ->
                                CompatibilidadeProdutoResponseDTO.builder()
                                        .id(c.getId())
                                        .modeloNome(c.getModelo().getNome())
                                        .marcaNome(c.getModelo().getMarca().getNome())
                                        .anoInicial(c.getAnoInicial())
                                        .anoFinal(c.getAnoFinal())
                                        .build()
                        ).toList() : null
                )
                .fotos(produto.getFotos() != null ?
                        produto.getFotos().stream().map(f ->
                                ProdutoFotoResponseDTO.builder()
                                        .id(f.getId())
                                        .url(f.getUrl())
                                        .build()
                        ).toList() : null
                )
                .build();
    }
}
