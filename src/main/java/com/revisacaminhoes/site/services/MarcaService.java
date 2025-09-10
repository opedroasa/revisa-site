package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.Marca;
import com.revisacaminhoes.site.repositories.MarcaRepository;
import com.revisacaminhoes.site.requestdto.MarcaRequestDTO;
import com.revisacaminhoes.site.responsedto.MarcaResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    // Criar nova marca
    public MarcaResponseDTO criarMarca(MarcaRequestDTO dto) {
        if (marcaRepository.existsByNome(dto.getNome())) {
            throw new RuntimeException("Já existe uma marca com esse nome!");
        }

        Marca marca = Marca.builder()
                .nome(dto.getNome())
                .ativo(true)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        Marca salva = marcaRepository.save(marca);
        return toResponseDTO(salva);
    }

    // Atualizar nome da marca
    public MarcaResponseDTO atualizarMarca(Long id, MarcaRequestDTO dto) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));

        marca.setNome(dto.getNome());
        marca.setAtualizadoEm(LocalDateTime.now());

        Marca atualizada = marcaRepository.save(marca);
        return toResponseDTO(atualizada);
    }

    // Inativar marca
    public void inativarMarca(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
        marca.setAtivo(false);
        marca.setAtualizadoEm(LocalDateTime.now());
        marcaRepository.save(marca);
    }

    // Ativar marca
    public void ativarMarca(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
        marca.setAtivo(true);
        marca.setAtualizadoEm(LocalDateTime.now());
        marcaRepository.save(marca);
    }

    // Excluir marca
    public void excluirMarca(Long id) {
        if (!marcaRepository.existsById(id)) {
            throw new RuntimeException("Marca não encontrada");
        }
        marcaRepository.deleteById(id);
    }

    // Listar todas as marcas
    public List<MarcaResponseDTO> listarMarcas() {
        return marcaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Listar todas as marcas ativas
    public List<MarcaResponseDTO> listarMarcasAtivas() {
        return marcaRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public Optional<MarcaResponseDTO> buscarPorId(Long id) {
        return marcaRepository.findById(id)
                .map(this::toResponseDTO);
    }

    // Conversão para DTO
    private MarcaResponseDTO toResponseDTO(Marca marca) {
        return MarcaResponseDTO.builder()
                .id(marca.getId())
                .nome(marca.getNome())
                .ativo(marca.getAtivo())
                .build();
    }
}
