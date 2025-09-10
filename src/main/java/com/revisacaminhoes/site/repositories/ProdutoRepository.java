package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
