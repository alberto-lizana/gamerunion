package com.segcaldessof.uniondegamers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segcaldessof.uniondegamers.model.UsuarioRol;

@Repository 
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {

    // Sin Borrar Al Usuario
    @Modifying
    @Query
    ("""
        DELETE FROM UsuarioRol ur WHERE ur.usuario.id = :usuarioId
    """)
    int borrarAsignacionesPorUsuarioId(@Param("usuarioId") Long usuarioId); 

    // Sin Borrar El Rol
    @Modifying
    @Query
    ("""
        DELETE FROM UsuarioRol ur WHERE ur.rol.id = :rolId
    """)
    int borrarAsignacionesPorRolId(@Param("rolId") Long rolId);

    // Borra un rol asignado a un usuario
    @Modifying
    @Query
    ("""
        DELETE FROM UsuarioRol ur
        WHERE ur.usuario.id = :usuarioId AND ur.rol.id = :rolId
    """)
    int borrarAsignacion(@Param("usuarioId") Long usuarioId, @Param("rolId") Long rolId);


}
