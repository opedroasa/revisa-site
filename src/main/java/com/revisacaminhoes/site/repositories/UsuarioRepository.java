package com.revisacaminhoes.site.repositories;

import com.revisacaminhoes.site.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import com.revisacaminhoes.site.entities.Role;
import org.springframework.data.domain.Pageable;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    // para compatibilidade durante a transição (tenta e-mail, senão username)
    Optional<Usuario> findByEmailOrUsername(String email, String username);

    boolean existsByEmail(String email);

    // para validar e-mail único em edição
    boolean existsByEmailAndIdNot(String email, Long id);

    // paginação com filtros opcionais
    @Query("""
  select u from Usuario u
  where (
          :q is null
          or lower(cast(u.email as string))    like concat('%', lower(cast(:q as string)), '%')
          or lower(cast(u.username as string)) like concat('%', lower(cast(:q as string)), '%')
        )
    and (:ativo is null or u.ativo = :ativo)
    and (:role  is null or u.role  = :role)
  """)
    Page<Usuario> search(
            @Param("q") String q,
            @Param("ativo") Boolean ativo,
            @Param("role") Role role,
            Pageable pageable
    );

    Optional<Usuario> findByResetToken(String token);


}
