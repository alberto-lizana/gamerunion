package com.segcaldessof.uniondegamers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segcaldessof.uniondegamers.model.Usuario;

@Repository 
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query
    ("""
        SELECT DISTINCT u
        FROM Usuario u
        LEFT JOIN FETCH u.asignacionesRoles ur
        LEFT JOIN FETCH ur.rol
    """)
    List<Usuario> findAllConRoles();

    @Query
    ("""
        SELECT u
        FROM Usuario u
        LEFT JOIN FETCH u.asignacionesRoles ur
        LEFT JOIN FETCH ur.rol
        WHERE u.id = :usuarioId
    """)
    Optional<Usuario> findByIdUsuario(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query
    ("""
        DELETE FROM Usuario u 
        WHERE u.id = :usuarioId
    """)
    int deleteByIdUsuario(@Param("usuarioId") Long usuarioId);

}
