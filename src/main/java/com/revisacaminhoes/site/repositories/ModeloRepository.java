package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.Marca;
import com.revisacaminhoes.site.entities.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository para a entidade Modelo.
 */
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    // Listar todos os modelos ativos
    List<Modelo> findByAtivoTrue();

    // Listar modelos de uma marca específica
    List<Modelo> findByMarcaId(Long marcaId);
}
