package com.segcaldessof.uniondegamers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segcaldessof.uniondegamers.model.Rol;

@Repository 
public interface RolRepository extends JpaRepository<Rol,Long> {

    @Modifying
    @Query
    ("""
        DELETE FROM Rol r
        WHERE r.id = :rolId
    """)
    int deleteByIdRol(@Param("rolId") Long rolId);

    @Query
    ("""
        SELECT r FROM Rol r
        WHERE r.nombre = :rolNombre
    """)
    Optional<Rol> findRolByNombre(@Param("rolNombre") String rolNombre);
}
