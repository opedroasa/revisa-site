package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository para a entidade Modelo.
 */
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    // Listar todos os modelos ativos
    List<Modelo> findByAtivoTrue();

    // Listar modelos de uma marca específica
    List<Modelo> findByMarcaId(Long marcaId);

    @Query("select m from Modelo m join fetch m.marca")
    List<Modelo> findAllWithMarca();

    @Query("select m from Modelo m join fetch m.marca where m.ativo = true")
    List<Modelo> findAllAtivosWithMarca();

    @Query("select m from Modelo m join fetch m.marca where m.marca.id = :marcaId")
    List<Modelo> findByMarcaIdWithMarca(@Param("marcaId") Long marcaId);

    @Query("select m from Modelo m join fetch m.marca where m.marca.id = :marcaId and m.ativo = true")
    List<Modelo> findByMarcaIdAndAtivoTrueWithMarca(@Param("marcaId") Long marcaId);
}
