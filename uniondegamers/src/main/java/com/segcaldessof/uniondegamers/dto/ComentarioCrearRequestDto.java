package com.segcaldessof.uniondegamers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComentarioCrearRequestDto(

    @NotNull(message = "El id del evento es obligatorio")
    Long eventoId,

    @NotBlank(message = "Para hacer un comentario debe existir caracteres que expresen lo que se quiere decir")
    @Size(min=1, max=1000, message= "La longitud del comentario tiene que estar entre 1 y 1000 caracteres")
    String contenido

) {}
