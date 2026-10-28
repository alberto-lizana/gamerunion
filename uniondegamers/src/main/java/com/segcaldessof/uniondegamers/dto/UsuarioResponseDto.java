package com.segcaldessof.uniondegamers.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Builder 
public record UsuarioResponseDto(

    Long id,
    String alias, 
    String nombre,
    String appat,
    String email,
    Boolean isActive,
    List<UsuarioRolResponseDto> asignacionesRoles 

) {}
