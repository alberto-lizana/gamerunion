package com.segcaldessof.uniondegamers.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.segcaldessof.uniondegamers.enums.RegionEnum;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder 
public record EventoResponseDto(

    Long id,
    String titulo,
    UsuarioResponseDto autorEvento,
    LocalDateTime fechaInicioEvento,
    LocalDateTime fechaTerminoEvento,
    String ubicacion,
    RegionEnum region,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaModificacion,
    Boolean isActive,
    List<ComentarioResponseDto> comentarios

) {}