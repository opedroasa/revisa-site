package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.CompatibilidadeProduto;
import com.revisacaminhoes.site.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompatibilidadeProdutoRepository extends JpaRepository<CompatibilidadeProduto, Long> {
    // Buscar produtos que servem para um modelo específico e ano
    @Query("SELECT cp.produto FROM CompatibilidadeProduto cp " +
            "WHERE cp.modelo.id = :modeloId " +
            "AND :ano BETWEEN cp.anoInicial AND cp.anoFinal")
    List<Produto> findProdutosCompatíveis(@Param("modeloId") Long modeloId, @Param("ano") Integer ano);
}