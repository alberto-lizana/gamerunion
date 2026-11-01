package com.segcaldessof.uniondegamers.mappers;

import com.segcaldessof.uniondegamers.dto.RolResponseDto;
import com.segcaldessof.uniondegamers.model.Rol;

public class RolMapper {

    public static RolResponseDto toDto(Rol r){
        return RolResponseDto
            .builder()
                .idRol(r.getId())
                .nombre(r.getNombre())
            .build();
    }

}
