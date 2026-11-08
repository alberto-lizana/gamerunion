package com.segcaldessof.uniondegamers.mappers;

import com.segcaldessof.uniondegamers.dto.EventoResponseDto;
import com.segcaldessof.uniondegamers.model.Evento;

public class EventoMapper {

    public static EventoResponseDto toDto(Evento e){
        return EventoResponseDto
            .builder()
                .id(e.getId())
                .titulo(e.getTitulo())
                .autorEvento(UsuarioMapper.toDto(e.getAutor()))
                .fechaInicioEvento(e.getFechaInicioEvento())
                .fechaTerminoEvento(e.getFechaTerminoEvento())
                .ubicacion(e.getUbicacion())
                .region(e.getRegion())
                .isActive(e.isActive())
                .fechaCreacion(e.getFechaCreacion())
                .fechaModificacion(e.getFechaModificacion())
            .build();
    }

}
