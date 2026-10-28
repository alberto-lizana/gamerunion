package com.segcaldessof.uniondegamers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Builder
public record UsuarioRolResponseDto(

    Long id,
    UsuarioResponseDto usuario,
    RolResponseDto rol
    
) {}
