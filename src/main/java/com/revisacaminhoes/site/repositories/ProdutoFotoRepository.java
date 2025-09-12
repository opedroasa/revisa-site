package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.ProdutoFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoFotoRepository extends JpaRepository<ProdutoFoto, Long> {
    List<ProdutoFoto> findByProdutoId(Long produtoId);
}
