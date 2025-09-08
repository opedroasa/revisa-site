package com.revisacaminhoes.site.services;

import com.revisacaminhoes.site.entities.Marca;
import com.revisacaminhoes.site.entities.Modelo;
import com.revisacaminhoes.site.repositories.MarcaRepository;
import com.revisacaminhoes.site.repositories.ModeloRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço com regras de negócio do Modelo.
 */
@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;

    public ModeloService(ModeloRepository modeloRepository, MarcaRepository marcaRepository) {
        this.modeloRepository = modeloRepository;
        this.marcaRepository = marcaRepository;
    }

    // Criar novo modelo
    public Modelo criarModelo(Modelo modelo) {
        // Verifica se a marca existe
        Marca marca = marcaRepository.findById(modelo.getMarca().getId())
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));

        modelo.setMarca(marca);
        modelo.setAtivo(true);
        modelo.setCriadoEm(LocalDateTime.now());
        modelo.setAtualizadoEm(LocalDateTime.now());

        return modeloRepository.save(modelo);
    }

    // Atualizar nome e ano do modelo
    public Modelo atualizarModelo(Long id, String novoNome, Integer novoAnoFabricacao) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));

        modelo.setNome(novoNome);
        modelo.setAnoFabricacao(novoAnoFabricacao);
        modelo.setAtualizadoEm(LocalDateTime.now());

        return modeloRepository.save(modelo);
    }

    // Inativar modelo
    public void inativarModelo(Long id) {
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo não encontrado"));

        modelo.setAtivo(false);
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
    public List<Modelo> listarModelos() {
        return modeloRepository.findAll();
    }

    // Listar todos os modelos (ativos)
    public List<Modelo> listarModelosAtivos() {
        return modeloRepository.findByAtivoTrue();
    }

    // Listar modelos de uma marca específica
    public List<Modelo> listarPorMarca(Long marcaId) {
        return modeloRepository.findByMarcaId(marcaId);
    }

    // Buscar modelo por ID
    public Optional<Modelo> buscarPorId(Long id) {
        return modeloRepository.findById(id);
    }

}
