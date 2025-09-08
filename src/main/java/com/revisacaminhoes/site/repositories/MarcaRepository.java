package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository para a entidade Marca.
 * Contém métodos para CRUD e buscas personalizadas.
 */
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    // Buscar todas as marcas ativas
    List<Marca> findByAtivoTrue();

    // Verificar se já existe marca com mesmo nome
    boolean existsByNome(String nome);
}
