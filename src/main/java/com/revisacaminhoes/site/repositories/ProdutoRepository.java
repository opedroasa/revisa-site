package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.Produto;
import com.revisacaminhoes.site.responsedto.ProdutoListItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // ----------------------------
    // Lista geral (sem filtros)
    // ----------------------------
    @Query("""
        select new com.revisacaminhoes.site.responsedto.ProdutoListItemDTO(
            p.id, p.nome, p.descricao, p.preco, p.estoque, p.ativo, null
        )
        from Produto p
        """)
    Page<ProdutoListItemDTO> findPageLight(Pageable pageable);

    @Query("""
        select new com.revisacaminhoes.site.responsedto.ProdutoListItemDTO(
            p.id, p.nome, p.descricao, p.preco, p.estoque, p.ativo, null
        )
        from Produto p
        where p.ativo = true
        """)
    Page<ProdutoListItemDTO> findPageLightAtivos(Pageable pageable);

    // ---------------------------------
    // Com filtros de marca/modelo/ano
    // ---------------------------------
    @Query("""
        select new com.revisacaminhoes.site.responsedto.ProdutoListItemDTO(
            p.id, p.nome, p.descricao, p.preco, p.estoque, p.ativo, null
        )
        from Produto p
        where
            (:somenteAtivos = false or p.ativo = true)
        and (
            (:marcaId is null and :modeloId is null and :ano is null)
            or exists (
                select 1
                from CompatibilidadeProduto cp
                  join cp.modelo m
                  join m.marca mc
                where cp.produto = p
                  and (:marcaId is null or mc.id = :marcaId)
                  and (:modeloId is null or m.id = :modeloId)
                  and (:ano is null or (cp.anoInicial <= :ano and cp.anoFinal >= :ano))
            )
        )
        """)
    Page<ProdutoListItemDTO> findPageLightFiltrado(
            @Param("marcaId") Long marcaId,
            @Param("modeloId") Long modeloId,
            @Param("ano") Integer ano,
            @Param("somenteAtivos") boolean somenteAtivos,
            Pageable pageable
    );

    // ----------------------------------------------------
    // Com filtros + busca por nome/descrição (q opcional)
    // ----------------------------------------------------
    @Query("""
        select new com.revisacaminhoes.site.responsedto.ProdutoListItemDTO(
            p.id, p.nome, p.descricao, p.preco, p.estoque, p.ativo, null
        )
        from Produto p
        where
            (:somenteAtivos = false or p.ativo = true)
        and (
            :q is null
            or trim(:q) = ''
            or lower(p.nome) like lower(concat('%', :q, '%'))
            or lower(p.descricao) like lower(concat('%', :q, '%'))
        )
        and (
            (:marcaId is null and :modeloId is null and :ano is null)
            or exists (
                select 1
                from CompatibilidadeProduto cp
                  join cp.modelo m
                  join m.marca mc
                where cp.produto = p
                  and (:marcaId is null or mc.id = :marcaId)
                  and (:modeloId is null or m.id = :modeloId)
                  and (:ano is null or (cp.anoInicial <= :ano and cp.anoFinal >= :ano))
            )
        )
        """)
    Page<ProdutoListItemDTO> searchPageLight(
            @Param("marcaId") Long marcaId,
            @Param("modeloId") Long modeloId,
            @Param("ano") Integer ano,
            @Param("q") String q,
            @Param("somenteAtivos") boolean somenteAtivos,
            Pageable pageable
    );
}
