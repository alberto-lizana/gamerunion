package com.segcaldessof.uniondegamers.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder 
public record ComentarioResponseDto( 

    Long id,
    String contenido,
    UsuarioResponseDto escritorComentario,
    Long eventoId,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaModificacion

){}