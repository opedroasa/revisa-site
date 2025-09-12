package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.CompatibilidadeProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompatibilidadeProdutoRepository extends JpaRepository<CompatibilidadeProduto, Long> {

    @Query("SELECT cp FROM CompatibilidadeProduto cp " +
            "JOIN FETCH cp.produto p " +
            "JOIN FETCH cp.modelo m " +
            "JOIN FETCH m.marca ma " +
            "WHERE (:marcaId IS NULL OR ma.id = :marcaId) " +
            "AND (:modeloId IS NULL OR m.id = :modeloId) " +
            "AND (:ano IS NULL OR :ano BETWEEN cp.anoInicial AND cp.anoFinal) " +
            "AND p.ativo = true")
    List<CompatibilidadeProduto> buscarProdutosCompatíveis(
            @Param("marcaId") Long marcaId,
            @Param("modeloId") Long modeloId,
            @Param("ano") Integer ano
    );
}
