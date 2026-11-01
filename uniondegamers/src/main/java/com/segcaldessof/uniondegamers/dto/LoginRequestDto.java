package com.segcaldessof.uniondegamers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder 
public record LoginRequestDto(

    @NotBlank(message = "Se requiere un username.")
    String username,

    @NotBlank(message = "Se requiere la contraseña.")
    String password

) {}