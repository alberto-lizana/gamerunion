package com.segcaldessof.uniondegamers.mappers;

import com.segcaldessof.uniondegamers.dto.ComentarioResponseDto;
import com.segcaldessof.uniondegamers.model.Comentario;

public class ComentarioMapper {

    public static ComentarioResponseDto toDto(Comentario c){
        return ComentarioResponseDto.builder()
                .id(c.getId())
                .contenido(c.getContenido())
                .escritorComentario(UsuarioMapper.toDto(c.getEscritor()))
                .fechaCreacion(c.getFechaCreacion())
                .fechaModificacion(c.getFechaModificacion())
                .eventoId(c.getEvento().getId())
            .build();
    }

}
