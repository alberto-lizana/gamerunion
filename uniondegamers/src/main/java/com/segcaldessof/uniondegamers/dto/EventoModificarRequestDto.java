package com.segcaldessof.uniondegamers.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.segcaldessof.uniondegamers.enums.RegionEnum;

public record EventoModificarRequestDto(

    Long eventoId,
    String titulo, 
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime fechaInicioEvento,

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime fechaTerminoEvento,
    Boolean isActive, 
    String ubicacion,
    RegionEnum region

) {}
