package com.segcaldessof.uniondegamers.mappers;

import com.segcaldessof.uniondegamers.dto.UsuarioRolResponseDto;
import com.segcaldessof.uniondegamers.model.UsuarioRol;

public class UsuarioRolMapper {

    public static UsuarioRolResponseDto toDto(UsuarioRol ur){
        return UsuarioRolResponseDto
            .builder()
                .rol(RolMapper.toDto(ur.getRol()))
            .build();
    }
}
