package com.segcaldessof.uniondegamers.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.segcaldessof.uniondegamers.model.Comentario;

@Repository 
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query
    ("""
        SELECT c
        FROM Comentario c
        JOIN FETCH c.escritor
        JOIN FETCH c.evento
        ORDER BY c.evento.fechaCreacion
    """)
    Page<Comentario> obtenerTodos(Pageable pageable);

    @Query
    ("""
        SELECT c
        FROM Comentario c
        JOIN FETCH c.escritor
        JOIN FETCH c.evento
        WHERE c.id = :idComentario
    """)
    Optional<Comentario> obtenerPorId(@Param("idComentario") Long idComentario);


    @Query
    ("""
        SELECT c
        FROM Comentario c
        JOIN FETCH c.escritor
        JOIN FETCH c.evento
        WHERE c.evento.id = :eventoId
        ORDER BY c.fechaCreacion DESC
    """)
    Page<Comentario> obtenerTodosPorEventoId(@Param("eventoId") Long eventoId, Pageable pageable);
}
