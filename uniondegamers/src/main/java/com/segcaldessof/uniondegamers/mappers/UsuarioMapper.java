package com.segcaldessof.uniondegamers.mappers;

import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;
import com.segcaldessof.uniondegamers.model.Usuario;

public class UsuarioMapper {

    public static UsuarioResponseDto toDto(Usuario us){
        return UsuarioResponseDto
            .builder()
                .id(us.getId())
                .username(us.getUsername())
                .nombre(us.getNombre())
                .appat(us.getAppat())
                .email(us.getEmail())
                .isActive(us.getIsActive())
                .asignacionesRoles(us.getAsignacionesRoles().stream().map(UsuarioRolMapper::toDto).toList())
            .build();
    }

}
