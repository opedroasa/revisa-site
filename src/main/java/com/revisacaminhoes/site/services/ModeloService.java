package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.requestdto.ModeloRequestDTO;
import com.revisacaminhoes.site.responsedto.ModeloResponseDTO;
import com.revisacaminhoes.site.entities.Marca;
import com.revisacaminhoes.site.entities.Modelo;
import com.revisacaminhoes.site.repositories.MarcaRepository;
import com.revisacaminhoes.site.repositories.ModeloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço com regras de negócio do Modelo.
 * Agora trabalha com DTOs para não expor entidades diretamente.
 */
@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;

    public ModeloService(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
    }

    // Criar novo modelo a partir do DTO
    public ModeloResponseDTO criarModelo(ModeloRequestDTO dto) {
        Marca marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));

        Modelo modelo = Modelo.builder()
                .nome(dto.getNome())
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .marca(marca)
                .build();

        Modelo salvo = modeloRepository.save(modelo);
        return toResponseDTO(salvo);
    }

    // Atualizar modelo existente
    public ModeloResponseDTO atualizarModelo(Long id, ModeloRequestDTO dto) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));

        modelo.setNome(dto.getNome());

        if (dto.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(dto.getMarcaId())
                    .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
            modelo.setMarca(marca);
        }

        modelo.setAtualizadoEm(LocalDateTime.now());

        Modelo atualizado = modeloRepository.save(modelo);
        return toResponseDTO(atualizado);
    }

    // Inativar modelo
    public void inativarModelo(Long id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));

        modelo.setAtivo(false);
        modelo.setAtualizadoEm(LocalDateTime.now());
        modeloRepository.save(modelo);

    }

    // Ativar modelo
    public void ativarModelo(Long id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));


        modelo.setAtivo(true);
        modelo.setAtualizadoEm(LocalDateTime.now());
        modeloRepository.save(modelo);
    }

    // Excluir modelo
    public void excluirModelo(Long id) {
        if (!modeloRepository.existsById(id)) {
            throw new RuntimeException("Modelo não encontrado");
        }
        modeloRepository.deleteById(id);
    }

    // Listar todos os modelos
    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> listarModelos() {
        var entidades = modeloRepository.findAllWithMarca();
        return entidades.stream().map(this::toResponseDTO).toList();
    }

    // Listar modelos ativos
    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> listarModelosAtivos() {
        var entidades = modeloRepository.findAllAtivosWithMarca();
        return entidades.stream().map(this::toResponseDTO).toList();
    }

    // Listar por marca
    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> listarPorMarca(Long marcaId) {
        var entidades = modeloRepository.findByMarcaIdWithMarca(marcaId);
        return entidades.stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ModeloResponseDTO> listarAtivosPorMarca(Long marcaId) {
        var entidades = modeloRepository.findByMarcaIdAndAtivoTrueWithMarca(marcaId);
        return entidades.stream().map(this::toResponseDTO).toList();
    }

    // Buscar por ID
    public Optional<ModeloResponseDTO> buscarPorId(Long id) {
        return modeloRepository.findById(id).map(this::toResponseDTO);
    }

    // Conversão para ResponseDTO
    private ModeloResponseDTO toResponseDTO(Modelo modelo) {
        return ModeloResponseDTO.builder()
                .id(modelo.getId())
                .nome(modelo.getNome())
                .ativo(modelo.getAtivo())
                .marcaNome(modelo.getMarca().getNome())
                .marcaId(modelo.getMarca().getId())
                .build();
    }
}
