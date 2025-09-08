package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.Marca;
import com.revisacaminhoes.site.repositories.MarcaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço com regras de negócio da Marca.
 * Inclui criação, atualização, inativação e listagem.
 */
@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;

    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    // Criar nova marca
    public Marca criarMarca(Marca marca) {
        // Evita duplicidade de nome
        if (marcaRepository.existsByNome(marca.getNome())) {
            throw new RuntimeException("Já existe uma marca com esse nome!");
        }
        marca.setAtivo(true);
        marca.setCriadoEm(LocalDateTime.now());
        marca.setAtualizadoEm(LocalDateTime.now());
        return marcaRepository.save(marca);
    }

    // Atualizar nome de marca existente
    public Marca atualizarMarca(Long id, String novoNome) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
        marca.setNome(novoNome);
        marca.setAtualizadoEm(LocalDateTime.now());
        return marcaRepository.save(marca);
    }

    // Inativar marca
    public void inativarMarca(Long id) {
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));
        marca.setAtivo(false);
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

    // Buscar todas as marcas
    public List<Marca> listarMarcas() {
        return marcaRepository.findAll();
    }

    // Buscar todas as marcas (ativas)
    public List<Marca> listarMarcasAtivas() {
        return marcaRepository.findByAtivoTrue();
    }

    // Buscar marca por ID
    public Optional<Marca> buscarPorId(Long id) {
        return marcaRepository.findById(id);
    }
}
