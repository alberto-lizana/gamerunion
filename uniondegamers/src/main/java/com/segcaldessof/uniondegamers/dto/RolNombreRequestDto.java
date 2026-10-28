package com.segcaldessof.uniondegamers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder 
public record RolNombreRequestDto(

    @NotBlank(message = "El id es obligatorio")
    @Min(1)
    Long idRol,

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre del rol debe tener entre 3 y 50 caracteres")
    String nombre

) {}
