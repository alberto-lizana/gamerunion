package com.segcaldessof.uniondegamers.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segcaldessof.uniondegamers.model.Evento;

@Repository 
public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query
    ("""
        SELECT e
        FROM Evento e
        JOIN FETCH e.autor
        ORDER BY e.fechaCreacion DESC
    """)
    Page<Evento> obtenerTodos(Pageable pageable);

    @Query
    ("""
        SELECT e
        FROM Evento e
        JOIN FETCH e.autor
        WHERE e.isActive = true
        ORDER BY e.fechaCreacion DESC
    """)
    Page<Evento> obtenerTodosActivos(Pageable pageable);

    @Query("""
        SELECT e
        FROM Evento e
        JOIN FETCH e.autor
        WHERE e.id = :eventoId 
    """)
    Optional<Evento> obtenerPorId(@Param("eventoId") Long eventoId);

    @Query("""
        SELECT e
        FROM Evento e
        JOIN FETCH e.autor
        WHERE e.id = :eventoId 
            AND e.isActive = true
    """)
    Optional<Evento> obtenerActivoPorId(@Param("eventoId") Long eventoId);
        
    @Modifying 
    @Query
    ("""
        DELETE     
        FROM Evento e 
        WHERE e.id = :eventoId
    """)
    int borrarPorId(@Param("eventoId") Long eventoId);

    
}
