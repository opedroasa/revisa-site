package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.ProdutoFoto;
import com.revisacaminhoes.site.repositories.projections.FotoDestaqueRow; // <- add
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <- add
import java.util.Collection;                     // <- add
import java.util.List;

public interface ProdutoFotoRepository extends JpaRepository<ProdutoFoto, Long> {

    // já existia
    List<ProdutoFoto> findByProdutoId(Long produtoId);

    // NOVO: 1 (no máximo) foto "destaque" por produto
    @Query(value = """
        SELECT DISTINCT ON (produto_id)
               produto_id AS produtoId,
               url
        FROM produto_foto
        WHERE produto_id IN (:ids)
          AND destaque = true
        ORDER BY produto_id, id ASC
        """, nativeQuery = true)
    List<FotoDestaqueRow> findDestaqueForProdutos(Collection<Long> ids);

    // NOVO (fallback): 1ª foto caso não haja destaque
    @Query(value = """
        SELECT DISTINCT ON (produto_id)
               produto_id AS produtoId,
               url
        FROM produto_foto
        WHERE produto_id IN (:ids)
        ORDER BY produto_id, id ASC
        """, nativeQuery = true)
    List<FotoDestaqueRow> findPrimeiraFotoForProdutos(Collection<Long> ids);
}
